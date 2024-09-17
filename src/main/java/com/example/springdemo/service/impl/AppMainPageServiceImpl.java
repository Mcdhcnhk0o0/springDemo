package com.example.springdemo.service.impl;

import com.example.springdemo.bean.NewsType;
import com.example.springdemo.bean.dao.UserDetail;
import com.example.springdemo.bean.dto.juhe.FortuneInfoDTO;
import com.example.springdemo.bean.dto.juhe.NewsInfoDTO;
import com.example.springdemo.bean.dto.juhe.WeatherInfoDTO;
import com.example.springdemo.bean.vo.AppMainPageVO;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.service.AppMainPageService;
import com.example.springdemo.service.JuheService;
import com.example.springdemo.service.UserDetailService;
import com.example.springdemo.service.UserService;
import com.example.springdemo.utils.BirthdayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Service
public class AppMainPageServiceImpl implements AppMainPageService {

    @Resource
    private UserDetailService userDetailService;

    @Resource
    private JuheService juheService;

    @Override
    public Result<AppMainPageVO> getAppMainPage(Long userId) {
        AppMainPageVO appMainPageVO = new AppMainPageVO();
        UserDetail userDetail = userDetailService.getUserDetail(userId);
        String birthday = userDetail.getBirthday();
        String address = userDetail.getAddress();
        CompletableFuture<FortuneInfoDTO> fortuneResp = CompletableFuture.supplyAsync(() -> {
            if (birthday != null) {
                String cons = BirthdayUtil.getConstellation(BirthdayUtil.parseDateFrom8BitBirthday(birthday));
                return juheService.getFortune(cons);
            } else {
                return null;
            }
        });
        CompletableFuture<WeatherInfoDTO> weatherResp = CompletableFuture.supplyAsync(() -> {
            if (address != null) {
                return juheService.getWeather(address);
            } else {
                return null;
            }
        });
        CompletableFuture<List<NewsInfoDTO>> newsResp = CompletableFuture.supplyAsync(() ->
                juheService.fetchTopNews(NewsType.TOP, 1, 30)
        );
        CompletableFuture<Void> mainPageTasks = CompletableFuture.allOf(fortuneResp, weatherResp, newsResp);
        mainPageTasks.join();
        try {
            appMainPageVO.setFortune(fortuneResp.get(3, TimeUnit.SECONDS));
            appMainPageVO.setWeather(weatherResp.get(3, TimeUnit.SECONDS));
            appMainPageVO.setNews(newsResp.get(3, TimeUnit.SECONDS));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new Result<AppMainPageVO>().success(appMainPageVO);
    }

}
