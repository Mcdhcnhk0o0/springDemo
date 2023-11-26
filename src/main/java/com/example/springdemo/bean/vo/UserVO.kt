package com.example.springdemo.bean.vo

import com.example.springdemo.bean.dao.User
import com.example.springdemo.bean.dao.UserDetail

data class UserVO (
    var user: User,
    val detail: UserDetail
)
