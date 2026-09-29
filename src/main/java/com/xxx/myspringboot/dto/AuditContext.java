package com.xxx.myspringboot.dto;

public class AuditContext {
    private static final ThreadLocal<AuditInfo> CONTEXT = new ThreadLocal<>();

    public static void set(AuditInfo info) {
        CONTEXT.set(info);
    }

    public static AuditInfo get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
