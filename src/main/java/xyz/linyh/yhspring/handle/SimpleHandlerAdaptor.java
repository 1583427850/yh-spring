package xyz.linyh.yhspring.handle;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.HttpMethod;
import xyz.linyh.yhspring.constant.RequestConstant;
import xyz.linyh.yhspring.context.MyApplicationContext;
import xyz.linyh.yhspring.entity.MyMethod;
import xyz.linyh.yhspring.entity.MyMethodParameter;
import xyz.linyh.yhspring.utils.ScanUtils;

import java.io.BufferedReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SimpleHandlerAdaptor implements HandlerAdaptor {

    /**
     * 用来处理请求参数和执行对应方法然后返回层序结束的结果
     *
     * @param request
     * @param response
     * @param handler
     * @throws Exception
     */

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        MyMethod handlerMethod = (MyMethod) handler;

//        获取里面的方法参数
        List<MyMethodParameter> methodParameters = handlerMethod.getMethodParameters();

//        获取pathPramers个数
        int pathParamNum = 0;
        for(MyMethodParameter methodParameter : methodParameters) {
            if (RequestConstant.PARAM_TYPE_PATH.equals(methodParameter.getParamType())) {
                pathParamNum++;
            }
        }

//        将里面所有的方法参数类型提取出来
        List<Class<?>> methodParameterTypes = new ArrayList<>();

        String bodyJson = null;
        for (MyMethodParameter methodParameter : methodParameters) {
            Class<?> type = methodParameter.getType();
            methodParameterTypes.add(type);
        }
        Method method = handlerMethod.getClassName().getMethod(handlerMethod.getMethodName(), methodParameterTypes.toArray(new Class<?>[0]));

        if (method == null) {
//            TODO 返回
            System.out.println("method is null");
            return null;
        }

        if (request.getMethod().equals(HttpMethod.POST)) {
            BufferedReader reader = request.getReader();
            String str, wholeStr = "";
            while ((str = reader.readLine()) != null) {
//                还需要加\n
                wholeStr += str + "\n";
            }
//            去掉最后的\n
            if (wholeStr != null && wholeStr.length() > 1) {
                wholeStr = wholeStr.substring(0, wholeStr.length() - 1);
            }

            bodyJson = JSONUtil.toJsonStr(wholeStr);
            System.out.println(bodyJson);
        }

        ArrayList<Object> methodParams = new ArrayList<>();

        for (MyMethodParameter methodParameter : methodParameters) {
            String name = methodParameter.getName();
            String paramType = methodParameter.getParamType();

            if (RequestConstant.PARAM_TYPE_BODY.equals(paramType)) {
                Object bean = jsonBodyToBean(bodyJson, methodParameter.getType());
                methodParams.add(bean);
            }

            if (RequestConstant.PARAM_TYPE_PARAM.equals(paramType)) {
                String parameter = request.getParameter(name);
                if (parameter == null && methodParameter.getRequire()) {
//                     TODO 如果参数是httpServletRequest或response这些，还需要给他注入，这些也需要特殊判断

//                    TODO 如果真的没有应该写一个返回前端的一个方法，后面统一加
                    System.out.println("参数不能为空");
//                    后面就没必要继续执行了
                }
                methodParams.add(parameter);

            }

            if (RequestConstant.PARAM_TYPE_PATH.equals(paramType)) {
//                需要获取请求路径后面携带的参数
                String requestURI = request.getRequestURI();
                String[] requestPath = requestURI.split("/");
                String[] srcPath = handlerMethod.getUrl().split("/");
                if(requestPath.length!=srcPath.length){
                    System.out.println("路径参数不匹配");
//                    TODO 统一返回错误信息
                }
                String s = requestPath[requestPath.length - pathParamNum--];
                methodParams.add(s);
            }


        }

        MyApplicationContext context = MyApplicationContext.getInstance();
        String name = ScanUtils.FirstWorldToLower(handlerMethod.getClassName().getSimpleName());
        Object bean = context.getBean(name);
        Object invoke = method.invoke(bean, methodParams.toArray());

//        TODO 后面还需要处理返回值给前端，直接先都转为json
        return JSONUtil.toJsonStr(invoke);
    }

    /**
     * 将请求的json数据转为对应需要传递给方法的对象
     *
     * @param bodyJson
     * @param type
     * @return
     */
    private Object jsonBodyToBean(String bodyJson, Class<?> type) {
        if (bodyJson == null) {
            System.out.println("bodyJson is null");
        }
        if (bodyJson == null || bodyJson.length() < 1) {
            return null;
        }
        try {

            return JSONUtil.toBean(bodyJson, type);
        } catch (Exception e) {
//            TODO 错误信息统一返回
            System.out.println("json转换错误");
        }
        return null;
    }
}
