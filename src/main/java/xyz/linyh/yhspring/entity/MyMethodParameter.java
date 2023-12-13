package xyz.linyh.yhspring.entity;

import lombok.Data;

@Data
public class MyMethodParameter {

    private String name;

    private Class<?> type;

    /**
     * 参数是get请求的参数还是post请求的参数
     */
    private boolean fromUrl;

    /**
     * 参数是否必须
     */
    private boolean require;


    private Object defaultValue;

}
