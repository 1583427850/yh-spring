package xyz.linyh.yhspring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用来获取配置文件中的值
 * @author lin
 */
@Target({ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface YhValue {

//    指定要获取配置文件哪一个配置，用.分开
    String value() default "";
}
