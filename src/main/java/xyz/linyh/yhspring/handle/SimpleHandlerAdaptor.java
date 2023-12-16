package xyz.linyh.yhspring.handle;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import xyz.linyh.yhspring.constant.RequestConstant;
import xyz.linyh.yhspring.context.MyApplicationContext;
import xyz.linyh.yhspring.entity.MultiPartFile;
import xyz.linyh.yhspring.entity.MyMethod;
import xyz.linyh.yhspring.entity.MyMethodParameter;
import xyz.linyh.yhspring.resolver.MyFileParamResolver;
import xyz.linyh.yhspring.utils.ScanUtils;

import java.io.BufferedReader;
import java.lang.invoke.VarHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

//        TODO 应该先把所有参数都解析看需要那个resolver，然后全部保存起来，然后一个一个解析后调用方法
        MyMethod handlerMethod = (MyMethod) handler;

//        获取里面的方法参数
        List<MyMethodParameter> methodParameters = handlerMethod.getMethodParameters();

//        获取pathParams个数
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
            log.error("找不到对应方法，{}", handlerMethod.getMethodName());
            return null;
        }

        if (request.getMethod().equals(HttpMethod.POST)&& request.getContentType().startsWith("application/json")) {
            BufferedReader reader = request.getReader();
            String str, wholeStr = "";
            while ((str = reader.readLine()) != null ) {
//                还需要加\n
                wholeStr += str + "\n";
            }
//            去掉最后的\n
            if (wholeStr != null && wholeStr.length() > 1) {
                wholeStr = wholeStr.substring(0, wholeStr.length() - 1);
            }

            bodyJson = JSONUtil.toJsonStr(wholeStr);
            log.info("请求体为:{}", bodyJson);
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

//                    TODO 如果真的没有应该写一个返回前端的一个方法，后面统一加
                    log.error("参数不能为空:{}", name);
//                    后面就没必要继续执行了
                }
                Object param = castToClass(parameter, methodParameter.getType());
                methodParams.add(param);

            }

            MyFileParamResolver myFileParamResolver = new MyFileParamResolver();
            if (myFileParamResolver.supports(methodParameter)) {
                Object file = myFileParamResolver.resolveArgument(methodParameter, request);
                if(file!=null){
                    methodParams.add((MultiPartFile)file);
                }else{
                    methodParams.add(null);
                }

            }

            if(RequestConstant.PARAM_TYPE_REQUEST.equals(paramType)){
                methodParams.add(request);
            }

            if(RequestConstant.PARAM_TYPE_RESPONSE.equals(paramType)){
                methodParams.add(response);
            }

            if (RequestConstant.PARAM_TYPE_PATH.equals(paramType)) {
//                需要获取请求路径后面携带的参数
                String requestURI = request.getRequestURI();
                String[] requestPath = requestURI.split("/");
                String[] srcPath = handlerMethod.getUrl().split("/");
                if(requestPath.length!=srcPath.length){
                    log.error("路径参数不匹配:{}", requestURI);
//                    TODO 统一返回错误信息
                }
                Class<?> type = methodParameter.getType();
                String s = requestPath[requestPath.length - pathParamNum--];
                Object param = castToClass(s,type);

                methodParams.add(param);
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
     * 讲String转为对应的实体类
     * @param s
     * @param type
     * @return
     */
    private Object castToClass(String s, Class<?> type) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method valueOf = null;
        try {
            valueOf = type.getMethod("valueOf",String.class);
        } catch (NoSuchMethodException e) {
            return s;
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
        Object invoke = valueOf.invoke(null, s);
        return invoke;
    }

    /**
     * 将请求的json数据转为对应需要传递给方法的对象
     *
     * @param bodyJson
     * @param type
     * @return
     */
    private Object jsonBodyToBean(String bodyJson, Class<?> type) {
        if (bodyJson == null || bodyJson.isEmpty()) {
            log.info("请求体为空");
        }
        try {

            return JSONUtil.toBean(bodyJson, type);
        } catch (Exception e) {
//            TODO 错误信息统一返回
            log.error("json转换错误,{}", e.getMessage());
        }
        return null;
    }
}
