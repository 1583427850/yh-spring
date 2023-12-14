package xyz.linyh.yhspring.handle;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdaptor {


    /**
     * TODO 返回值还没确定,先用string替代
     * <p>
     * 获取handler里面的一些参数，然后在request里面解析出来，然后去调用
     *
     * @param request
     * @param response
     * @param handler
     * @throws Exception
     */
    String handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
