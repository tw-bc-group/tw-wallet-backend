package com.thoughtworks.wallet.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * The class JacksonUtil
 *
 * json字符与对像转换
 *
 * @version: $Revision$ $Date$ $LastChangedBy$
 */
@Slf4j
@Component
public final class JacksonUtil {

    public static ObjectMapper objectMapper;

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
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        try {
            return objectMapper.readValue(jsonStr, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * json数组转List
     * @param jsonStr
     * @param valueTypeRef
     * @return
     */
    public static <T> T jsonStrToBean(String jsonStr, TypeReference<T> valueTypeRef){
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

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
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String parsePropertyFromJson(String json, String property) throws JsonProcessingException {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        final JsonNode jsonNode = objectMapper.readTree(json);
        return jsonNode.get(property).toString();
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