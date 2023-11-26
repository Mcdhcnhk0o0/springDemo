package com.example.springdemo.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper
import com.example.springdemo.bean.dao.UserDetail
import com.example.springdemo.mapper.UserDetailMapper
import com.example.springdemo.service.UserDetailService
import org.springframework.stereotype.Service
import javax.annotation.Resource


@Service
class UserDetailServiceImpl: UserDetailService {

    @Resource
    var userDetailMapper: UserDetailMapper? = null

    override fun addUserDetail(
        userId: Long,
        gender: String?,
        birthday: String?,
        avatar: String?,
        address: String?,
        introduction: String?
    ): Boolean {
        val userDetail = UserDetail()
        userDetail.userId = userId
        userDetail.gender = gender
        userDetail.birthday = birthday
        userDetail.avatar = avatar
        userDetail.address = address
        userDetail.introduction = introduction
        userDetailMapper?.insert(userDetail)
        return true
    }

    override fun getUserDetail(userId: Long): UserDetail {
        val wrapper: QueryWrapper<UserDetail> = QueryWrapper()
        wrapper.eq("user_id", userId)
        return userDetailMapper?.selectOne(wrapper) ?: throw RuntimeException("user not exist!")
    }

    override fun modifyUserDetail(
        userId: Long,
        gender: String?,
        birthday: String?,
        avatar: String?,
        address: String?,
        introduction: String?
    ): Boolean {
        val userDetail = getUserDetail(userId) ?: return false
        userDetail.gender = gender
        userDetail.birthday = birthday
        userDetail.avatar = avatar
        userDetail.address = address
        userDetail.introduction = introduction
        val wrapper = UpdateWrapper<UserDetail>()
        wrapper.eq("user_id", userId)
        userDetailMapper?.update(userDetail, wrapper)
        return true
    }


}