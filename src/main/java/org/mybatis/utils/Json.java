package org.mybatis.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Json {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object object) {
        mapper.setDateFormat(simpleDateFormat);
        String json = "";
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static <T> T copyObject(Object source, Class<?> target) {
        return toObject(toJson(source), target);
    }

    public static <T> T toObject(String json, Class<?> cls) {
        mapper.setDateFormat(simpleDateFormat);
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return (T) mapper.readValue(json, cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(Object fromValue, TypeReference<?> toValueTypeRef) {
        mapper.setDateFormat(simpleDateFormat);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return (T) mapper.convertValue(fromValue, toValueTypeRef);
    }
}
