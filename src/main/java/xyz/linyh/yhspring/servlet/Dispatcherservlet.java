package xyz.linyh.yhspring.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

public class Dispatcherservlet implements Servlet {
    public void doDispatch() {
        // 1.获取请求的url
        // 2.根据url获取对应的handler
        // 3.根据handler获取对应的controller
        // 4.根据handler获取对应的方法
        // 5.根据handler获取对应的参数
        // 6.根据参数获取对应的值
        // 7.执行方法
        // 8.返回结果
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
