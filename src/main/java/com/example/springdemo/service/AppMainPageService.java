package com.example.springdemo.service;

import com.example.springdemo.bean.vo.AppMainPageVO;
import com.example.springdemo.bean.vo.protocol.Result;

public interface AppMainPageService {

    Result<AppMainPageVO> getAppMainPage(Long userId);

}
