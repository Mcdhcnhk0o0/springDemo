package com.example.springdemo.nacos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;


@Data
@AllArgsConstructor
public class NacosConfig {

    public enum Type {
        JSON,
        TEXT
    }

    private String dataId;
    private String groupId;

    @Override
    public int hashCode() {
        return Objects.hash(dataId, groupId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NacosConfig that = (NacosConfig) o;
        return dataId.equals(that.dataId) && groupId.equals(that.groupId);
    }

}
