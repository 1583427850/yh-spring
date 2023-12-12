package xyz.linyh.yhspring.entity;

import lombok.Data;

@Data
public class MyMethodParameter {

    private String name;

    private Class<?> type;

    private Object defaultValue;

}
