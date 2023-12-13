package xyz.linyh.yhspring.handle;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SimpleHandlerAdaptor implements HandlerAdaptor {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMapping hm = null;
        if (handler instanceof HandlerMapping) {
            hm = (HandlerMapping) handler;
        } else {
            return;
        }







    }
}
