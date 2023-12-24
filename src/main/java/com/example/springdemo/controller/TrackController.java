package com.example.springdemo.controller;

import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.dao.TrackEvent;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.service.TrackService;
import com.example.springdemo.utils.UserInfoUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/track")
@CrossOrigin
public class TrackController {

    @Resource
    private TrackService trackService;

    @UserLoginToken
    @PostMapping("/upload")
    public Result<Boolean> uploadTrackEvent(
            @RequestHeader(value = "token") String token,
            @RequestBody TrackEvent trackEvent
    ) {
        Long userId = UserInfoUtil.parseUserIdFromToken(token);
        if (!Objects.equals(trackEvent.getUserId(), userId)) {
            return new Result<Boolean>().message("非法userId").fail();
        }
        trackService.uploadTrackEvent(trackEvent);
        return new Result<Boolean>().success(true);
    }

    @UserLoginToken
    @GetMapping("/get")
    public Result<List<TrackEvent>> getTrackEvent(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "eventType") int eventType,
            @RequestParam(value = "pageNum") int pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        Long userId = UserInfoUtil.parseUserIdFromToken(token);
        return trackService.getRecentTrackEvent(userId, eventType, pageNum, pageSize);
    }

}
