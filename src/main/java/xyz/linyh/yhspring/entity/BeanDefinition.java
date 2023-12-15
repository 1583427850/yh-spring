package xyz.linyh.yhspring.entity;

import lombok.Data;

/**
 * bean对象保存的定义信息
 * @author lin
 */
@Data
public class BeanDefinition {

    /**
     * 这个bean全局唯一的id
     */
    private String beanName;

    /**
     * 这个bean的class对象
     */
    private Class<?> beanClass;

    /**
     * 这个bean的class的全限定名
     */
    private String beanClassName;

    /**
     * 这个bean的实例对象
     */
    private Object instance;



}
