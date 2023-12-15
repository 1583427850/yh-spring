package xyz.linyh.yhspring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author lin
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface YhService {
    String value() default "";
}
