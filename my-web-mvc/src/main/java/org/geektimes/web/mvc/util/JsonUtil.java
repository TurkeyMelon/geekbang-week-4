package org.geektimes.web.mvc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtil {

    private JsonUtil() {
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串。
     *
     * @param data 数据
     * @return json字符串
     */
    public static String objectToJson(Object data) {
        try {
            return OBJECT_MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
