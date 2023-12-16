package xyz.linyh.yhspring.resolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import xyz.linyh.yhspring.entity.MyMethod;
import xyz.linyh.yhspring.entity.MyMethodParameter;

import java.io.IOException;

/**
 * TODO 解析方法参数的，后面需要在adaptor里面获取不同参数的解析器，然后去解析对应参数出来比较好
 */
public interface HandlerMethodArgumentResolver {

    /**
     * 查看是否支持这个参数解析
     * @param parameter
     * @return
     */
    boolean supports(MyMethodParameter parameter);

    /**
     * TODO 简易版
     * 解析出request里面的参数内容
     * @param parameter
     * @return
     */
    Object resolveArgument(MyMethodParameter parameter, HttpServletRequest request) throws ServletException, IOException;



}
