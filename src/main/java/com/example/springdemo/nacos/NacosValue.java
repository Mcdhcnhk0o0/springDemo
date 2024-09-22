package com.example.springdemo.nacos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class NacosValue {

    private Object data;
    private NacosConfig.Type type;

    public static NacosValue from(String value) {
        Map<String, Object> dataMap = NacosUtil.tryParseJson(value);
        if (dataMap == null) {
            return new NacosValue(value, NacosConfig.Type.TEXT);
        }
        return new NacosValue(dataMap, NacosConfig.Type.JSON);
    }

}
