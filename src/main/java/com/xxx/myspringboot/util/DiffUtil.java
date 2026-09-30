package com.xxx.myspringboot.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class DiffUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 不参与 diff 的系统字段 / 审计字段
     */
    private static final Set<String> IGNORED_FIELDS = Set.of(
            "id",
            "createdTime", "createdUserId", "createdUserName",
            "updatedTime", "updatedUserId", "updatedUserName",
            "isDeleted", "revision", "version"
    );

    private DiffUtil() {
    }

    /**
     * 对比更新前后的 JSON，返回字段级差异。
     * 返回结构：{ "字段名": { "old": 旧值, "new": 新值 }, ... }
     */
    public static Map<String, Object> diff(String oldJson, String newJson) {
        if (oldJson == null || newJson == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> oldObj = parse(oldJson);
        Map<String, Object> newObj = parse(newJson);
        if (oldObj == null || newObj == null) {
            return Collections.emptyMap();
        }

        // 兼容 MyBatis-Plus 的 ParamMap：优先取 et，其次 param1
        oldObj = unwrap(oldObj);
        newObj = unwrap(newObj);

        Map<String, Object> changes = new LinkedHashMap<>();

        // 1. 新对象里变化的 / 新增的字段
        for (Map.Entry<String, Object> entry : newObj.entrySet()) {
            String key = entry.getKey();
            if (IGNORED_FIELDS.contains(key)) {
                continue;
            }
            Object newVal = entry.getValue();
            Object oldVal = oldObj.get(key);

            if (!Objects.equals(oldVal, newVal)) {
                changes.put(key, buildChange(oldVal, newVal));
            }
        }

        // 2. 旧对象有、新对象没有的字段（被删除的字段）
        for (Map.Entry<String, Object> entry : oldObj.entrySet()) {
            String key = entry.getKey();
            if (IGNORED_FIELDS.contains(key)) {
                continue;
            }
            if (!newObj.containsKey(key)) {
                changes.put(key, buildChange(entry.getValue(), null));
            }
        }

        return changes;
    }

    /**
     * 解析 JSON 为 Map，失败返回 null
     */
    private static Map<String, Object> parse(String json) {
        try {
            return MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 兼容 MyBatis-Plus ParamMap：
     * { "et": {...}, "param1": {...} }  →  { ... }（返回 et 的内容）
     * 普通实体 JSON 原样返回。
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> unwrap(Map<String, Object> map) {
        // 普通实体：顶层直接是业务字段，不含 et / param1
        if (!map.containsKey("et") && !map.containsKey("param1")) {
            return map;
        }
        Object et = map.get("et");
        if (et instanceof Map) {
            return (Map<String, Object>) et;
        }
        Object param1 = map.get("param1");
        if (param1 instanceof Map) {
            return (Map<String, Object>) param1;
        }
        return map;
    }

    private static Map<String, Object> buildChange(Object oldVal, Object newVal) {
        Map<String, Object> change = new LinkedHashMap<>();
        change.put("old", oldVal);
        change.put("new", newVal);
        return change;
    }
}