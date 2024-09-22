package com.example.springdemo.nacos;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class NacosUtil {

    @Nullable
    static Map<String, Object> tryParseJson(String json) {
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            return jsonObject.getInnerMap();
        } catch (JSONException ex) {
            try {
                JSONArray jsonArray = JSON.parseArray(json);
                throw new RuntimeException("use JSONArray instead of JSONObject");
            } catch (JSONException ex2) {
                return null;
            }
        }
    }

    @NotNull
    static String[] parseConfigKey(@Nonnull String configKey) {
        if (configKey.contains(".")) {
            if (configKey.contains("-")) {
                throw new IllegalArgumentException("configKey cannot contain both '.' and '-'");
            }
            return configKey.split("\\.");
        }
        return configKey.split("-");
    }

    @NotNull
    static String[] parseDataConfigKey(@Nonnull String dataConfigKey) {
        String[] dataGroup = dataConfigKey.split("/");
        if (dataGroup.length < 2 || dataGroup.length > 3) {
            throw new IllegalArgumentException("invalid dataConfigKey format");
        }
        return parseConfigKey(dataGroup[dataGroup.length - 1]);
    }

    @NotNull
    static NacosConfig parseConfigFromDataConfigKey(@Nonnull String dataConfigKey) {
        String[] dataGroup = dataConfigKey.split("/");
        if (dataGroup.length == 2) {
            return new NacosConfig(dataGroup[0], null);
        } else if (dataGroup.length == 3) {
            return new NacosConfig(dataGroup[0], dataGroup[1]);
        }
        throw new IllegalArgumentException("invalid dataConfigKey format");
    }

    @SuppressWarnings("unchecked")
    static String findKeyFromMap(Map<String, Object> candidate, String[] keySection) {
        for (int i = 0; i < keySection.length; i++) {
            if (candidate.containsKey(keySection[i])) {
                if (i == keySection.length - 1) {
                    return candidate.get(keySection[i]).toString();
                } else if (candidate.get(keySection[i]) instanceof Map) {
                    candidate = (Map<String, Object>) candidate.get(keySection[i]);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        return null;
    }

}
