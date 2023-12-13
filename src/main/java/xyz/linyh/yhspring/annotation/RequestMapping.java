package xyz.linyh.yhspring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lin
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    String value() default "";

    String must() default "false";
}