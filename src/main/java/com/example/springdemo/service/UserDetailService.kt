package com.example.springdemo.service

import com.example.springdemo.bean.dao.UserDetail

interface UserDetailService {

    fun addUserDetail(userId: Long, gender: String?, birthday: String?, avatar: String?, address: String?, introduction: String?): Boolean

    fun getUserDetail(userId: Long): UserDetail

    fun modifyUserDetail(userId: Long, gender: String?, birthday: String?, avatar: String?, address: String?, introduction: String?): Boolean

}