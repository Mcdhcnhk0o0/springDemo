package com.example.springdemo.nacos;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class NacosTemplate {

    public String getConfig(String dataId, String groupId) {
        NacosConfig config = new NacosConfig(dataId, groupId);
        if (NacosClient.getInstance().getConfig(config) == null) {
            try {
                return NacosClient.getInstance().listen(config);
            } catch (Exception e) {
                log.error(e.getMessage());
                return "";
            }
        }
        return NacosClient.getInstance().getConfig(config);
    }

    /**
     * Get config from nacos
     * @param dataConfigKey dataId/groupId/configKey
     * @return config value
     */
    public String getConfig(String dataConfigKey) {
        return NacosClient.getInstance().getConfig(dataConfigKey);
    }

    public String getConfig(String dataId, String groupId, String configKey) {
        if (configKey == null) {
            return getConfig(dataId, groupId);
        }
        NacosConfig config = new NacosConfig(dataId, groupId);
        return NacosClient.getInstance().getConfig(config, configKey);
    }

}
