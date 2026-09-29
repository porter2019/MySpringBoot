package com.xxx.myspringboot.dto.event;

import lombok.Data;

/**
 * event事件实体标记
 */
@Data
public class Demo1Event {
    public Demo1Event(String name) {
        this.name = name;
    }

    private String name;
}
