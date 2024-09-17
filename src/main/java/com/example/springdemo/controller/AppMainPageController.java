package com.example.springdemo.controller;

import com.example.springdemo.annotation.PassToken;
import com.example.springdemo.annotation.UserLoginToken;
import com.example.springdemo.bean.NewsType;
import com.example.springdemo.bean.dto.juhe.FortuneInfoDTO;
import com.example.springdemo.bean.dto.juhe.NewsInfoDTO;
import com.example.springdemo.bean.dto.juhe.WeatherInfoDTO;
import com.example.springdemo.bean.vo.AppMainPageVO;
import com.example.springdemo.bean.vo.protocol.Result;
import com.example.springdemo.service.AppMainPageService;
import com.example.springdemo.service.JuheService;
import com.example.springdemo.utils.UserInfoUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/app/main")
@CrossOrigin
public class AppMainPageController {

    @Resource
    private JuheService juheService;

    @Resource
    private AppMainPageService appMainPageService;

    @UserLoginToken
    @GetMapping("/get")
    public Result<AppMainPageVO> getMainPage(
            @RequestHeader(value = "token") String token
    ) {
        Long userId = UserInfoUtil.parseUserIdFromToken(token);
        return appMainPageService.getAppMainPage(userId);
    }

    @PassToken
    @GetMapping("/news/get")
    public Result<List<NewsInfoDTO>> getRecentNews(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        NewsType newsType = NewsType.fromString(type);
        if (newsType == null) {
            newsType = NewsType.TOP;
        }
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 30;
        }
        List<NewsInfoDTO> newsList = juheService.fetchTopNews(newsType, pageNum, pageSize);
        return new Result<List<NewsInfoDTO>>().success(newsList);
    }

    @PassToken
    @GetMapping("/weather/get")
    public Result<WeatherInfoDTO> getWeather(
            @RequestParam(value = "city") String city
    ) {
        WeatherInfoDTO resp = juheService.getWeather(city);
        return new Result<WeatherInfoDTO>().success(resp);
    }

    @PassToken
    @GetMapping("/fortune/get")
    public Result<FortuneInfoDTO> getFortune(
            @RequestParam(value = "cons") String cons
    ) {
        FortuneInfoDTO resp = juheService.getFortune(cons);
        return new Result<FortuneInfoDTO>().success(resp);
    }

}
