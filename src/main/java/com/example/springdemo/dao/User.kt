package com.example.springdemo.dao

data class User(
    var userId: Long?,
    var email: String?,
    var userName: String?,
    var userNickname: String?,
    var password: String?,
    var extraInfo: String?
) {
    constructor() :
            this(null, null, null, null, null, null)
}