package xyz.linyh.yhspring.servlet;


import cn.hutool.json.JSONUtil;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import xyz.linyh.yhspring.entity.MultiPartFile;
import xyz.linyh.yhspring.handle.HandlerAdaptor;
import xyz.linyh.yhspring.handle.HandlerMapping;
import xyz.linyh.yhspring.handle.SimpleHandlerAdaptor;
import xyz.linyh.yhspring.handle.SimpleHandlerMapping;
import xyz.linyh.yhspring.resolver.MyFileParamResolver;

/**
 * TODO 简易版
 * 请求统一接收和转发类
 * @author lin
 */
@WebServlet(name = "dispatcherservlet", urlPatterns = "/**")
@Slf4j
public class Dispatcherservlet extends HttpServlet {
    public void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        统一设置请求编码和响应编码
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        System.out.println(request.getRequestURI());

        YhHandlerExecutionChain handler = getHandler(request);

        if (handler == null) {
            noHandlerFound(request, response);
            return;
        }

//        可以去执行接口对应的方法
        HandlerAdaptor handlerAdaptor = getAdaptor(handler, request);

//        利用adaptor执行对应方法，然后获取到返回值
        String result = handlerAdaptor.handle(request, response, handler.getHandlerMethod());

//        TODO 都只返回json 后续会根据注解判断返回什么类型
        Class<?> returnType = handler.getHandlerMethod().getReturnType();
       String voidReturn = "void";
        if (!returnType.getName().equals(voidReturn)){
            System.out.println("返回结果为:" + result);
            response.getWriter().print(JSONUtil.toJsonStr(result));
        }


    }

    protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void systemErrorReturn(HttpServletResponse response) throws Exception {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
        try {

            doDispatch(req, resp);

        } catch (Exception e) {
            log.error("doGet error:{}", e.getMessage());
            systemErrorReturn(resp);
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            log.info("doPost error:{}", e.getMessage());
            e.printStackTrace();
            systemErrorReturn(resp);
        }
    }
}
