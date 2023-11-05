package com.example.springdemo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springdemo.bean.response.LLMResponse;
import okhttp3.HttpUrl;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;


public class SparkLLMUtil {

    // 鉴权方法
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();
        return httpUrl.toString();
    }

    public static boolean llmResponseValid(LLMResponse response) {
        return response.header.code == 0;
    }

    public static boolean canAddHistory(List<LLMResponse.RoleContent> historyList) {
        int historyLength = 0;
        for(LLMResponse.RoleContent temp:historyList){
            historyLength = historyLength + temp.getContent().length();
        }
        // 由于历史记录最大上线1.2W左右，需要判断是能能加入历史
        if(historyLength > 12000){
            historyList.remove(0);
            historyList.remove(1);
            historyList.remove(2);
            historyList.remove(3);
            historyList.remove(4);
            return false;
        }
        return true;
    }

    public static String getAuthRequest(String appid, String completeAnswer, List<LLMResponse.RoleContent> historyList) {
        JSONObject requestJson=new JSONObject();
        // header参数
        JSONObject header = new JSONObject();
        header.put("app_id", appid);
        header.put("uid", UUID.randomUUID().toString().substring(0, 10));
        // parameter参数
        JSONObject parameter = new JSONObject();
        JSONObject chat = new JSONObject();
        chat.put("domain", "generalv2");
        chat.put("temperature", 0.5);
        chat.put("max_tokens", 4096);
        parameter.put("chat", chat);
        // payload参数
        JSONObject payload = new JSONObject();
        JSONObject message = new JSONObject();
        JSONArray text = new JSONArray();
        // 历史问题获取
        if (historyList.size() > 0){
            for (LLMResponse.RoleContent tempRoleContent: historyList) {
                text.add(JSON.toJSON(tempRoleContent));
            }
        }
        // 最新问题添加
        LLMResponse.RoleContent roleContent = new LLMResponse.RoleContent();
        roleContent.setRole("user");
        roleContent.setContent(completeAnswer);
        text.add(JSON.toJSON(roleContent));
        historyList.add(roleContent);
        // json组装
        message.put("text", text);
        payload.put("message", message);
        requestJson.put("header", header);
        requestJson.put("parameter", parameter);
        requestJson.put("payload", payload);
        System.err.println(requestJson.toString());
        return requestJson.toString();
    }

}
