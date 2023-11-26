package com.example.springdemo.bean.vo.protocol

enum class ResponseInfo(val code: Int, val message: String) {
    DEFAULT(0, "未知状态"),
    SUCCESS(200, "请求成功"),
    FAIL(400, "服务器错误")
}


enum class LoginResultInfo(val code: Int, val message: String) {
    USER_NOT_EXIST(400001, "用户不存在"),
    WRONG_PASSWORD(400002, "用户名与密码不匹配"),
    LOGIN_SUCCESS(200001, "登录成功"),
    USER_ALREADY_LOGIN(400003, "用户重复登录"),
    USER_NOT_LOGIN(200002, "用户还未登录"),
    LOGOUT_SUCCESS(200003, "注销成功"),
}


enum class ArticleResultInfo(val code: Int, val message: String) {
    ARTICLE_NOT_EXIST(400101, "文章不存在"),
    ARTICLE_CREATE_SUCCESS(200101, "文章创建成功"),
    ARTICLE_UPDATE_SUCCESS(200102, "文章更新成功")
}