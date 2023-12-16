package xyz.linyh.yhspring.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author lin
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface YhRequestMapping {

    String value() default "";

    String must() default "false";
}
