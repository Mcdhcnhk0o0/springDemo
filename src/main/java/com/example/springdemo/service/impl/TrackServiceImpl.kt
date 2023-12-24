package com.example.springdemo.service.impl

import com.baomidou.dynamic.datasource.annotation.DS
import com.baomidou.mybatisplus.core.conditions.Wrapper
import com.baomidou.mybatisplus.core.toolkit.Wrappers
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.example.springdemo.bean.dao.TrackEvent
import com.example.springdemo.bean.vo.protocol.Result
import com.example.springdemo.mapper.TrackMapper
import com.example.springdemo.service.TrackService
import com.example.springdemo.service.helper.TrackServiceHelper
import org.springframework.stereotype.Service
import javax.annotation.Resource


@DS("chat")
@Service
class TrackServiceImpl: TrackService {

    @Resource
    private var trackMapper:TrackMapper? = null

    override fun uploadTrackEvent(trackEvent: TrackEvent?): Result<Boolean> {
        trackEvent?.serverTimestamp = System.currentTimeMillis().toString()
        trackMapper?.insert(trackEvent)
        return Result<Boolean>().success(true)
    }

    override fun getRecentTrackEvent(
        userId: Long?,
        eventCode: Int?,
        pageNum: Int,
        pageSize: Int
    ): Result<List<TrackEvent>> {
        val page: Page<TrackEvent> = Page<TrackEvent>(pageNum.toLong(), pageSize.toLong())
        // 这里有两个问题：
        // 1. kotlin的::运算符与Java不同，其返回的是KMutableProperty1
        // 2. 这里并没有真正调用lambda表达式，因此传入lambda表达式也无效
        val queryWrapper: Wrapper<TrackEvent> = Wrappers.lambdaQuery<TrackEvent>()
            .eq(TrackServiceHelper.getJavaLambdaOfUserId(), userId)
            .or()
            .eq(TrackServiceHelper.getJavaLambdaOfEventCode(), eventCode)
            .orderByDesc(TrackServiceHelper.getJavaLambdaOfLocalTimestamp())
        val eventList = trackMapper?.selectPage(page, queryWrapper)?.records
        return Result<List<TrackEvent>>().success(eventList)
    }

    override fun getTrackEventByDate(userId: Long?, eventId: Int?, date: String?): Result<List<TrackEvent>>? {
        return Result<List<TrackEvent>>().success()
    }

}