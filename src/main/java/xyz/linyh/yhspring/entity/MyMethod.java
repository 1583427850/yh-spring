package xyz.linyh.yhspring.entity;

import lombok.Data;

import java.util.List;

/**
 * @author lin
 */
@Data
public class MyMethod {

    /**
     * 类class
     */
    private Class<?> className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 参数类型
     */
    private List<MyMethodParameter> methodParameters;

    /**
     * 返回值类型
     */
    private Class<?> returnType;


}

