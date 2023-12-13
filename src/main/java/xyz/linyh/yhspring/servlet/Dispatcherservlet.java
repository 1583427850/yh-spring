package xyz.linyh.yhspring.servlet;


import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.HttpMethod;
import lombok.SneakyThrows;
import xyz.linyh.yhspring.entity.MyMethod;
import xyz.linyh.yhspring.entity.MyMethodParameter;
import xyz.linyh.yhspring.handle.HandlerAdaptor;
import xyz.linyh.yhspring.handle.HandlerMapping;
import xyz.linyh.yhspring.handle.SimpleHandlerMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "dispatcherservlet", urlPatterns = "/**")
public class Dispatcherservlet extends HttpServlet {
    public void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
//         1.获取请求的url
        System.out.println(request.getRequestURI());
//         2.根据url获取对应的handler
        YhHandlerExecutionChain handler = getHandler(request);
//         3. 获取adaptor
//        可以去执行接口对应的方法
        HandlerAdaptor handlerAdaptor = getAdaptor(handler, request);

        // 3.根据handler获取对应的controller
        // 4.根据handler获取对应的方法
        // 5.根据handler获取对应的参数
        // 6.根据参数获取对应的值
        // 7.执行方法
        // 8.返回结果
    }

    /**
     * TODO 简易版
     *
     * @param handler
     * @return
     */
    private HandlerAdaptor getAdaptor(YhHandlerExecutionChain handler, HttpServletRequest request) throws Exception {
        MyMethod handlerMethod = handler.getHandlerMethod();
//        获取里面的方法参数
        List<MyMethodParameter> methodParameters = handlerMethod.getMethodParameters();
//        将里面所有的方法参数类型提取出来
        List<Class<?>> methodParameterTypes = new ArrayList<>();

        JSONObject bodyJson = null;
        for (MyMethodParameter methodParameter : methodParameters) {
            Class<?> type = methodParameter.getType();
            methodParameterTypes.add(type);
        }
        Method method = handlerMethod.getClassName().getMethod(handlerMethod.getMethodName(), methodParameterTypes.toArray(new Class<?>[0]));

        if(method==null){
//            TODO
            System.out.println("method is null");
        }

        if(request.getMethod().equals(HttpMethod.POST)){
            BufferedReader reader = request.getReader();
            String str, wholeStr = "";
            while ((str = reader.readLine()) != null) {
                wholeStr += str;
            }
            bodyJson = JSONUtil.parseObj(wholeStr);
        }

        ArrayList<Object> methodParams= new ArrayList<>();
        for (MyMethodParameter methodParameter : methodParameters) {
            String parameterName = methodParameter.getName();
//            TODO 判断他是get请求的参数还是post请求的参数
            Object param = getParam(parameterName, request.getMethod());
            methodParams.add(param);

        }
//        TODO 差传入的参数
        method.invoke(handlerMethod.getClassName().newInstance(), null);

//        参数需要根据参数列表获取然后传入

        for (MyMethodParameter methodParameter : methodParameters) {
            Class<?> type = methodParameter.getType();

        }

//        判断请求方法，如果是post，那么就需要获取请求体参数
        if("POST".equals(request.getMethod())){
//            getRequestBody(request);
        }

//        request.getParameter()
        try {
            BufferedReader reader = request.getReader();
            String str, wholeStr = "";
            while ((str = reader.readLine()) != null) {
                wholeStr += str;
            }
            JSONObject entries = JSONUtil.parseObj(wholeStr);
            System.out.println(entries.get("hello"));
//            System.out.println(wholeStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        获取request里面对应的参数

        return null;
    }

    private Object getParam(String parameterName, String method) {
        return null;
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
