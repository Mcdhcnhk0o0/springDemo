package com.example.springdemo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.springdemo.bean.dto.SunoClipDTO;
import com.example.springdemo.bean.dto.SunoGenerationRequest;
import com.example.springdemo.bean.dto.SunoGenerationDTO;
import com.example.springdemo.bean.dto.message.DataMessage;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.service.HttpService;
import com.example.springdemo.service.SunoService;
import com.example.springdemo.service.WebSocketService;
import com.example.springdemo.service.helper.SunoScheduleHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Slf4j
@Service
public class SunoServiceImpl implements SunoService {

    private static final String innerServerUrl = "http://127.0.0.1:8000/";

    @Resource
    private HttpService httpService;

    @Resource
    private WebSocketService webSocketService;

    @Override
    public Result<SunoGenerationDTO> generateByPrompt(Long userId, String prompt, boolean await) {
        String url = innerServerUrl + "generate/description-mode";
        Map<String, Object> params = new HashMap<>();
        params.put("gpt_description_prompt", prompt);
        params.put("make_instrumental", false);
        params.put("mv", "chirp-v3-0");
        params.put("prompt", "");
        return getSunoGenerationDTOResult(userId, await, url, params);
    }

    @Override
    public Result<SunoGenerationDTO> generateByCustom(Long userId, SunoGenerationRequest request, boolean await) {
        String url = innerServerUrl + "generate";
        Map<String, Object> params = new HashMap<>();
        params.put("prompt", request.getLyric());
        params.put("title", request.getTitle());
        params.put("tags", request.getTags());
        return getSunoGenerationDTOResult(userId, await, url, params);
    }

    private Result<SunoGenerationDTO> getSunoGenerationDTOResult(Long userId, boolean await, String url, Map<String, Object> params) {
        String responseBody = httpService.post(url, params).getData();
        log.info("task response from suno: \n" + responseBody.substring(0, Math.min(responseBody.length(), 200)));
        SunoGenerationDTO generationDTO = JSONObject.parseObject(responseBody, SunoGenerationDTO.class);
        if (await && userId != null) {
            List<SunoClipDTO> fullClips = awaitGeneration(userId, generationDTO.getClips()).getData();
            generationDTO.setClips(fullClips);
        }
        return new Result<SunoGenerationDTO>().success(generationDTO);
    }

    @Override
    public Result<List<SunoClipDTO>> awaitGeneration(Long userId, List<SunoClipDTO> generationList) {
        // 首先push任务流水线数据
        webSocketService.sendToUser(userId, new DataMessage.Builder().tag("submitted").data(generationList).build());
        log.info("任务已提交，待拉取数目为:" + generationList.size());
        // 线程池并行轮询任务状态，确定已运行
        List<SunoClipDTO> clipList = new ArrayList<>();
        List<SunoClipDTO> syncClipList = Collections.synchronizedList(clipList);
        boolean running = SunoScheduleHelper.instance().scheduleEach(generationList, (timer, countDownLatch, clip) -> {
            SunoClipDTO currentClip = getGenerationById(clip.getId()).getData();
            if (currentClip != null && Objects.equals("streaming", currentClip.getStatus())) {
                // 任务状态切换至streaming时，push当前状态
                log.info(currentClip.getId() + " is streaming into " + currentClip.getAudioUrl());
                webSocketService.sendToUser(userId, new DataMessage.Builder().tag("streaming").data(currentClip).build());
                syncClipList.add(currentClip);
                countDownLatch.countDown();
                timer.cancel();
            }
        }, 5000L, 5000L, 60);
        syncClipList.clear();
        // 继续轮询任务状态，直至生成任务完成
        boolean complete = SunoScheduleHelper.instance().scheduleEach(generationList, (timer, countDownLatch, clip) -> {
            SunoClipDTO currentClip = getGenerationById(clip.getId()).getData();
            if (Objects.equals(currentClip.getStatus(), "complete")) {
                // 任务状态切换至complete时，push当前状态
                log.info(clip.getId() + " is complete!");
                webSocketService.sendToUser(userId, new DataMessage.Builder().tag("complete").data(currentClip).build());
                syncClipList.add(currentClip);
                countDownLatch.countDown();
                timer.cancel();
            }
        }, 45 * 1000L, 5 * 1000L, 240);
        // 等待并在成功完成后返回所有数据
        if (complete) {
            return new Result<List<SunoClipDTO>>().success(syncClipList);
        }
        throw new IllegalArgumentException("server internal error");
    }


    @Override
    public Result<List<SunoClipDTO>> getGenerationHistory(int pageNum) {
        String url = innerServerUrl + "feeds/" + pageNum;
        String responseBody = httpService.get(url).getData();
        log.info("history response from suno: \n" + responseBody.substring(0, Math.min(responseBody.length(), 200)));
        List<SunoClipDTO> generationDTO = JSONObject.parseArray(responseBody, SunoClipDTO.class);
        return new Result<List<SunoClipDTO>>().success(generationDTO);
    }

    @Override
    public Result<SunoClipDTO> getGenerationById(String id) {
        String url = innerServerUrl + "feed/" + id;
        String responseBody = httpService.get(url).getData();
        log.info("generation feed response from suno: \n" + responseBody.substring(0, Math.min(responseBody.length(), 200)));
        List<SunoClipDTO> generationDTO = JSONObject.parseArray(responseBody, SunoClipDTO.class);
        if (generationDTO != null && generationDTO.size() > 0) {
            return new Result<SunoClipDTO>().success(generationDTO.get(0));
        } else {
            throw new IllegalStateException("got no generation");
        }
    }

}
