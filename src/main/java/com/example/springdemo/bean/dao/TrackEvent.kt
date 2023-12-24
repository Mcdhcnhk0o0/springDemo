package com.example.springdemo.bean.dao

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName


@TableName("track_info")
data class TrackEvent (
    @TableId(type = IdType.AUTO)
    var id: Long? = null,
    var userId: Long? = null,
    var deviceId: String? = null,
    var eventType: Int? = null,
    var eventCode: String? = null,
    var eventBody: String? = null,
    var localTimestamp: String? = null,
    var serverTimestamp: String? = null
) {
    constructor():
            this(null, null, null, null, null, null, null, null)
}