package xyz.linyh.yhspring.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author lin
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface YhConfiguration {
}
