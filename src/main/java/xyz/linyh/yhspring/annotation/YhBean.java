package xyz.linyh.yhspring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface YhBean {

    /**
     * 可以指定注册到bean容器时候的名字，如果没有指定，就是方法名字
     *
     */
    String value() default "";
}
