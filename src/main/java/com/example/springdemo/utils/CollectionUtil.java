package com.example.springdemo.utils;

import java.util.Collection;
import java.util.List;

public class CollectionUtil {

    public static <T> T getNullableSingleton(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return collection.iterator().next();
    }

    public static <T> T getNullableFirst(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

}
