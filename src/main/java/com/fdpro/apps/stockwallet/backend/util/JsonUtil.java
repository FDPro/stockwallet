package com.fdpro.apps.stockwallet.backend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      .findAndRegisterModules();

    public static <T> T fromJson(String json, Class<T> resultClass) throws IOException {
        return OBJECT_MAPPER.readValue(json, resultClass);
    }

    public static String toJson(Object value) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(value);
    }
}
