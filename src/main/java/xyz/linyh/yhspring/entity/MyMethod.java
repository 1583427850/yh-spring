package xyz.linyh.yhspring.entity;

import lombok.Data;

import java.util.List;

/**
 * @author lin
 */
@Data
public class MyMethod {

    private Class<?> className;

    private String methodName;

    private String url;

    private String requestMethod;

    private List<MyMethodParameter> methodParameters;

    private Class<?> returnType;


}

