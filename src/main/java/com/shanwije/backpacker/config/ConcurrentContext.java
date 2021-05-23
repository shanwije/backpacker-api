package com.shanwije.backpacker.config;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ConcurrentContext {

    @NonNull
    private static Map<String, String> contextMap = new ConcurrentHashMap<>();

    public static void put(String key, String val) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        } else {
            contextMap.put(key, val);
        }
    }

    public static String get(String key) {
        return contextMap.get(key);
    }

    public static void remove(String key) {
        contextMap.remove(key);
    }

    public static void clear() {
        contextMap.clear();
    }

    public static void setContextMap(Map<String, String> newContextMap) {
        contextMap = new HashMap<>(newContextMap);
    }

    public static Map<String, String> getCopyOfContextMap() {
        return new HashMap<>(contextMap);
    }

}

