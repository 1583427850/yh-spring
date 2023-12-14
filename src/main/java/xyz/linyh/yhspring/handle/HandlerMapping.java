package xyz.linyh.yhspring.handle;

import jakarta.servlet.http.HttpServletRequest;
import xyz.linyh.yhspring.servlet.YhHandlerExecutionChain;

import java.util.List;

public interface HandlerMapping {


    /**
     * 传入controller，解析里面带有特定注解的方法
     *
     * @param controllerClass
     * @return
     */
    void buildMapping(List<Class<?>> controllerClass);

    YhHandlerExecutionChain getHandler(HttpServletRequest request);


}
