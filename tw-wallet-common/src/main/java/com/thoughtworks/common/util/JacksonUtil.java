package com.thoughtworks.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * The class JacksonUtil
 * <p>
 * json字符与对像转换
 *
 * @version: $Revision$ $Date$ $LastChangedBy$
 */
@Slf4j
@Component
public final class JacksonUtil {

    public static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();

        // Don't throw an exception when json has extra fields you are
        // not serializing on. This is useful when you want to use a pojo
        // for deserialization and only care about a portion of the json
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Ignore null values when writing json.
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Write times as a String instead of a Long so its human readable.
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());

        // Custom serializer for coda hale metrics.
        //TODO: add metrics
//        mapper.registerModule(new MetricsModule(TimeUnit.MINUTES, TimeUnit.MILLISECONDS, false));
    }
    //endregion

    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
     * (1)转换为普通JavaBean：jsonStrToBean(json,Student.class)
     * (2)转换为List,如List<Student>,将第二个参数传递为Student
     * [].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List
     *
     * @param jsonStr
     * @param valueType
     * @return
     */
    public static <T> T jsonStrToBean(String jsonStr, Class<T> valueType) {
        try {
            return objectMapper.readValue(jsonStr, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * json数组转List
     *
     * @param jsonStr
     * @param valueTypeRef
     * @return
     */
    public static <T> T jsonStrToBean(String jsonStr, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(jsonStr, valueTypeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 把JavaBean转换为json字符串
     *
     * @param object
     * @return
     */
    public static String beanToJSonStr(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String parsePropertyFromJson(String json, String property) throws JsonProcessingException {
        final JsonNode jsonNode = objectMapper.readTree(json);
        final JsonNode node = jsonNode.get(property);

        if (node instanceof TextNode) {
            return node.textValue();
        }

        return node.toString();
    }

    public String readJsonFile(String fileName) throws IOException {
        String jsonStr;

        final InputStream resourceAsStream = this.getClass().getResourceAsStream(fileName);
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8));

        int ch;
        StringBuilder stringBuffer = new StringBuilder();
        while ((ch = bufferedReader.read()) != -1) {
            stringBuffer.append((char) ch);
        }
        bufferedReader.close();
        jsonStr = stringBuffer.toString();
        return jsonStr;
    }
}