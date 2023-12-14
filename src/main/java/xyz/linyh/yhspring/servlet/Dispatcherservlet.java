package xyz.linyh.yhspring.servlet;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import xyz.linyh.yhspring.handle.HandlerAdaptor;
import xyz.linyh.yhspring.handle.HandlerMapping;
import xyz.linyh.yhspring.handle.SimpleHandlerAdaptor;
import xyz.linyh.yhspring.handle.SimpleHandlerMapping;

@WebServlet(name = "dispatcherservlet", urlPatterns = "/**")
public class Dispatcherservlet extends HttpServlet {
    public void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println(request.getRequestURI());

        YhHandlerExecutionChain handler = getHandler(request);

        if (handler == null) {
//            TODO 统一返回错误信息
            System.out.println("handler is null");
            return;
        }

//        可以去执行接口对应的方法
        HandlerAdaptor handlerAdaptor = getAdaptor(handler, request);

//        利用adaptor执行对应方法，然后获取到返回值
        String result = handlerAdaptor.handle(request, response, handler.getHandlerMethod());
        System.out.println("返回结果为:" + result);


    }

    /**
     * TODO 简易版
     *
     * @param handler
     * @return
     */
    private HandlerAdaptor getAdaptor(YhHandlerExecutionChain handler, HttpServletRequest request) throws Exception {

        return new SimpleHandlerAdaptor();


    }



    /**
     * TODO 简易版
     * 会返回一个handlerChain 里面会包含一些执行这个接口需要经过的一些拦截器什么的
     *  根据请求获取对应handler处理
     *
     * @param request
     * @return
     */
    private YhHandlerExecutionChain getHandler(HttpServletRequest request) {
        HandlerMapping simpleHandlerMapping = SimpleHandlerMapping.getInstance();
        return simpleHandlerMapping.getHandler(request);
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("doGet");
        doDispatch(req, resp);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("doPost");
        doDispatch(req, resp);
    }
}
