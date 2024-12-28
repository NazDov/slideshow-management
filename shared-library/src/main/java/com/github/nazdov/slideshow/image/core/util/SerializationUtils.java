package com.github.nazdov.slideshow.image.core.util;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;
import java.io.UncheckedIOException;

public class SerializationUtils {

    public static ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .registerModule(new ParameterNamesModule());


    public static void setObjectMapper(ObjectMapper objectMapper) {
        SerializationUtils.OBJECT_MAPPER = objectMapper;
    }


    public static String serializeObjectToJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public static <T> T deserializeJsonString(String request, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(request, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T deserializeJsonString(String request, TypeReference<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(request, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
