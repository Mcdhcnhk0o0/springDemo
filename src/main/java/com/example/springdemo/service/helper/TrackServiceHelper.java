package com.example.springdemo.service.helper;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.example.springdemo.bean.dao.TrackEvent;

public class TrackServiceHelper {

    public static SFunction<TrackEvent, Long> getJavaLambdaOfUserId() {
        return TrackEvent::getUserId;
    }

    public static SFunction<TrackEvent, String> getJavaLambdaOfEventCode() {
        return TrackEvent::getEventCode;
    }

    public static SFunction<TrackEvent, String> getJavaLambdaOfLocalTimestamp() {
        return TrackEvent::getLocalTimestamp;
    }

}
