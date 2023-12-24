package com.example.springdemo.service;

import com.example.springdemo.bean.dao.TrackEvent;
import com.example.springdemo.bean.vo.protocol.Result;

import java.util.List;

public interface TrackService {

    Result<Boolean> uploadTrackEvent(TrackEvent trackEvent);

    Result<List<TrackEvent>> getRecentTrackEvent(Long userId, Integer eventCode, int pageNum, int pageSize);

    Result<List<TrackEvent>> getTrackEventByDate(Long userId, Integer eventCode, String date);

}
