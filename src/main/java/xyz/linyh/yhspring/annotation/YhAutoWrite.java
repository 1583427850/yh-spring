package xyz.linyh.yhspring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author lin
 */
@Target(ElementType.FIELD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface YhAutoWrite {
    String value() default "";
}
