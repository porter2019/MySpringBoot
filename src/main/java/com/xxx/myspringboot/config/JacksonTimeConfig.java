package com.xxx.myspringboot.config;

import org.springframework.boot.jackson.JacksonComponent;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@JacksonComponent
public class JacksonTimeConfig {

    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ========== LocalDateTime 序列化器 ==========
    public static class LocalDateTimeSerializer extends ValueSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializationContext ctxt)
                throws JacksonException {
            if (value == null) {
                gen.writeNull();
            } else {
                gen.writeString(DATE_TIME_FORMATTER.format(value));
            }
        }
    }

    // ========== LocalDateTime 反序列化器 ==========
    public static class LocalDateTimeDeserializer extends ValueDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt)
                throws JacksonException {
            String text = p.getString();
            if (text == null || text.isEmpty()) {
                return null;
            }
            return LocalDateTime.parse(text, DATE_TIME_FORMATTER);
        }
    }

    // ========== LocalDate 序列化器 ==========
    public static class LocalDateSerializer extends ValueSerializer<LocalDate> {
        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializationContext ctxt)
                throws JacksonException {
            if (value == null) {
                gen.writeNull();
            } else {
                gen.writeString(DATE_FORMATTER.format(value));
            }
        }
    }

    // ========== LocalDate 反序列化器 ==========
    public static class LocalDateDeserializer extends ValueDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt)
                throws JacksonException {
            String text = p.getString();
            if (text == null || text.isEmpty()) {
                return null;
            }
            return LocalDate.parse(text, DATE_FORMATTER);
        }
    }

    // ========== LocalTime 序列化器 ==========
    public static class LocalTimeSerializer extends ValueSerializer<LocalTime> {
        @Override
        public void serialize(LocalTime value, JsonGenerator gen, SerializationContext ctxt)
                throws JacksonException {
            if (value == null) {
                gen.writeNull();
            } else {
                gen.writeString(TIME_FORMATTER.format(value));
            }
        }
    }

    // ========== LocalTime 反序列化器 ==========
    public static class LocalTimeDeserializer extends ValueDeserializer<LocalTime> {
        @Override
        public LocalTime deserialize(JsonParser p, DeserializationContext ctxt)
                throws JacksonException {
            String text = p.getString();
            if (text == null || text.isEmpty()) {
                return null;
            }
            return LocalTime.parse(text, TIME_FORMATTER);
        }
    }
}

