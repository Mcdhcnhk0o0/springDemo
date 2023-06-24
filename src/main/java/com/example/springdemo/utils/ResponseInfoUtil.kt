package com.example.springdemo.utils

class ResponseInfoUtil {

    companion object {
        var code: MutableList<Int> = listOf<Int>(
                200,

        ) as MutableList<Int>
    }

}

enum class ResponseInfo(val code: Int, val message: String) {
    DEFAULT(0, "未知状态"),
    SUCCESS(200, "请求成功"),
    FAIL(400, "服务器错误")
}
