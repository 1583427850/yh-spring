package xyz.linyh.yhspring.handle;

import java.util.List;

public interface HandlerMapping {

    /**
     * 传入controller，解析里面带有特定注解的方法
     * @param controllerClass
     * @return
     */
    List buildMapping(List<Class<?>> controllerClass);

}
