package com.example.springdemo.nacos;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executor;



@Slf4j
@Component
public class NacosClient {

    private NacosClient() {}

    public static NacosClient getInstance() {
        return InnerClass.INSTANCE;
    }

    private ConfigService configService;
    private final Map<NacosConfig, NacosValue> configMap = new HashMap<>();
    private final Map<String, Object> valueMap = new HashMap<>();

    public void init() throws NacosException {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, "8.141.90.135:8848");
        properties.setProperty(PropertyKeyConst.USERNAME, "nacos");
        properties.setProperty(PropertyKeyConst.PASSWORD, "xxxxxx");
        NamingFactory.createNamingService(properties);
        configService = ConfigFactory.createConfigService(properties);
    }

    String listen(NacosConfig config) throws NacosException {
        String value = configService.getConfigAndSignListener(config.getDataId(), config.getGroupId(), 5000, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("Refresh nacos config info: " + configInfo);
                configMap.put(config, NacosValue.from(configInfo));
            }
        });
        log.info("Get nacos config info: " + value);
        configMap.put(config, NacosValue.from(value));
        return value;
    }

    public void initAndListen() {
        try {
            init();
        } catch (NacosException e) {
            log.error(e.getErrMsg());
        }
        if (configService == null) {
            log.error("cannot get nacos config service");
            return;
        }
        try {
            listen(new NacosConfig("llm.authorization", "LLM_SERVICE"));
            listen(new NacosConfig("third.authorization", "THIRD_SERVICE"));
        } catch (NacosException e) {
            log.error(e.getErrMsg());
        }
    }

    /**
     * Get config from nacos
     * @param config dataId and groupId
     * @return config value
     */
    public String getConfig(NacosConfig config) {
        if (config == null || configMap.get(config) == null) {
            return null;
        }
        return configMap.get(config).getData().toString();
    }

    /**
     * Get config from nacos
     * @param config dataId and groupId
     * @param configKey key of json, split by "."
     * @return config value
     */
    @SuppressWarnings("unchecked")
    public String getConfig(NacosConfig config, String configKey) {
        if (config == null || configKey == null) {
            return null;
        }
        if (!configMap.containsKey(config)) {
            return null;
        }
        String[] configKeySections = NacosUtil.parseConfigKey(configKey);
        Map<String, Object> candidate = new HashMap<>((Map<String, Object>) configMap.get(config).getData());
        return NacosUtil.findKeyFromMap(candidate, configKeySections);
    }

    /**
     * Get config from nacos
     * @param dataConfigKey dataId/groupId/configKey, groupId is optional
     * @return config value
     */
    @SuppressWarnings("unchecked")
    public String getConfig(String dataConfigKey) {
        if (dataConfigKey == null) {
            return null;
        }
        NacosConfig config = NacosUtil.parseConfigFromDataConfigKey(dataConfigKey);
        Map<String, Object> candidate = new HashMap<>();
        if (config.getGroupId() == null) {
            for (NacosConfig nacosConfig : configMap.keySet()) {
                if (Objects.equals(nacosConfig.getDataId(), config.getDataId())) {
                    candidate.putAll((Map<String, Object>) configMap.get(nacosConfig).getData());
                    break;
                }
            }
        } else {
            if (configMap.containsKey(config)) {
                candidate.putAll((Map<String, Object>) configMap.get(config).getData());
            }
        }
        String[] configKeySections = NacosUtil.parseDataConfigKey(dataConfigKey);
        return NacosUtil.findKeyFromMap(candidate, configKeySections);
    }


    private static class InnerClass {
        private static final NacosClient INSTANCE = new NacosClient();
    }

}
