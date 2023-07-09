package com.example.springdemo.service.third;

import com.baidu.aip.ocr.AipOcr;
import com.example.springdemo.bean.Result;
import com.example.springdemo.config.BaiduOcrConfig;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;


@Service
public class BaiduOcrService {

    static {
        System.out.println("BaiduOcrService is initialized");
    }

    @Resource
    private BaiduOcrConfig customConfig;

    private String APP_ID;
    private String API_KEY;
    private String API_SECRET;

    private AipOcr client;

    private static class InnerClass {
        static {
            System.out.println("BaiduOcrService#InnerClass is initialized");
        }
        private static final BaiduOcrService instance = new BaiduOcrService();
    }

    public void init() {}

//    public static BaiduOcrService getInstance() {
//        return InnerClass.instance;
//    }

    public JSONObject getOcrResultWhenPicInUrl(String url) {
        if (client == null) {
            initializeOcrClient();
        }
        return client.basicGeneralUrl(url, new HashMap<String, String>());
    }

    private void getBaiduOcrConfig() {
        APP_ID = customConfig.getAppId();
        API_KEY = customConfig.getApiKey();
        API_SECRET = customConfig.getApiSecret();
    }

    private void initializeOcrClient() {
//        client = new AipOcr(APP_ID, API_KEY, API_SECRET);
        client = new AipOcr(customConfig.getAppId(), customConfig.getApiKey(), customConfig.getApiSecret());
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
    }
}
