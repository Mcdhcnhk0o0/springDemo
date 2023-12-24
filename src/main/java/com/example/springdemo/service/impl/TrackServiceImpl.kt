package com.example.springdemo.service.impl

import com.baomidou.dynamic.datasource.annotation.DS
import com.baomidou.mybatisplus.core.toolkit.Wrappers
import com.baomidou.mybatisplus.core.toolkit.support.SFunction
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.example.springdemo.bean.dao.TrackEvent
import com.example.springdemo.bean.vo.protocol.Result
import com.example.springdemo.mapper.TrackMapper
import com.example.springdemo.service.TrackService
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
        val page = Page<TrackEvent>(pageNum.toLong(), pageSize.toLong())
        val wrapper = Wrappers.lambdaQuery<TrackEvent>()
            .eq(SFunction<TrackEvent, Any> { o: TrackEvent -> o.userId }, userId)
            .eq(SFunction<TrackEvent, Any> { o: TrackEvent -> o.eventCode }, eventCode)
            .orderByDesc(SFunction<TrackEvent, Any> { obj: TrackEvent -> obj.localTimestamp })
        val eventList = trackMapper?.selectPage(page, wrapper)?.records
        return Result<List<TrackEvent>>().success(eventList)
    }

    override fun getTrackEventByDate(userId: Long?, eventId: Int?, date: String?): Result<List<TrackEvent>>? {
        return Result<List<TrackEvent>>().success()
    }

}