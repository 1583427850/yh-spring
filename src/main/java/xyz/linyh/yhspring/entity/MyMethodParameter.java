package xyz.linyh.yhspring.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MyMethodParameter {

    private String name;

    private Class<?> type;

    /**
     * 参数是get请求的参数还是post请求的参数
     */
    private String from;

    /**
     * 参数是否必须
     */
    private Boolean require;

    /**
     * 请求参数传递在请求的地方类型
     */
    private String paramType;


    private Object defaultValue;

}
