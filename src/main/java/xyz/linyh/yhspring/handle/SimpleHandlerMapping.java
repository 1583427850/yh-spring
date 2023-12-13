package xyz.linyh.yhspring.handle;

import jakarta.servlet.http.HttpServletRequest;
import xyz.linyh.yhspring.annotation.GetMapping;
import xyz.linyh.yhspring.annotation.PostMapping;
import xyz.linyh.yhspring.annotation.RequestBody;
import xyz.linyh.yhspring.annotation.RequestMapping;
import xyz.linyh.yhspring.entity.MyMethod;
import xyz.linyh.yhspring.entity.MyMethodParameter;
import xyz.linyh.yhspring.servlet.YhHandlerExecutionChain;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lin
 */
public class SimpleHandlerMapping implements HandlerMapping{

    /**
     *  保存每一个controller里面的所有方法和里面的所有接口地址
      */
    List<MyMethod> allMappingMethod = new ArrayList<>();

    private SimpleHandlerMapping() {
    }

    private static class SimpleHandlerMappingHolder{
        private static final SimpleHandlerMapping INSTANCE = new SimpleHandlerMapping();
    }

    public static SimpleHandlerMapping getInstance(){
        return SimpleHandlerMappingHolder.INSTANCE;
    }



    /**
     * 传入controller，解析里面带有特定注解的方法
     *
     * @param controllerClass
     * @return
     */
    @Override
    public List buildMapping(List<Class<?>> controllerClass) {
        List<MyMethod> myMethods = new ArrayList<>();
//        扫描里面所有的方法，然后保存到属性里面
        for (Class<?> aClass : controllerClass) {
            String prefixUrl = null;
            if (aClass.isAnnotationPresent(RequestMapping.class) || aClass.isAnnotationPresent(GetMapping.class) || aClass.isAnnotationPresent(PostMapping.class)) {
                prefixUrl = getControllerMappingUrl(aClass);
            }

            List<Method> controllerMappingMethod = getControllerMappingMethod(aClass);
//            创建一个自己的method类，将里面的参数保存到这里面
            for (Method method : controllerMappingMethod) {
                String mappingUrl = getMethodMappingUrl(method);
                MyMethod myMethod = new MyMethod();
                String url = prefixUrl==null ? mappingUrl : prefixUrl + mappingUrl;
                myMethod.setUrl(url);
                myMethod.setMethodName(method.getName());
                myMethod.setReturnType(method.getReturnType());
                myMethod.setMethodParameters(getMethodParameters(method));
                myMethod.setRequestMethod(getRequestMethod(method));
                myMethod.setClassName(aClass);
                myMethods.add(myMethod);
            }
        }
        allMappingMethod = myMethods;
        return myMethods;
    }

    @Override
    public YhHandlerExecutionChain getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        for (MyMethod myMethod : allMappingMethod) {
            if (myMethod.getUrl().equals(requestURI) && myMethod.getRequestMethod().equals(method)) {
                return new YhHandlerExecutionChain(myMethod);
            }
        }
        return null;
    }

    private String getRequestMethod(Method method) {
//        TODO 后面扩展为可以设置多个请求方式
        boolean hasGetMapping= method.isAnnotationPresent(GetMapping.class);
        boolean hasPostMapping = method.isAnnotationPresent(PostMapping.class);
        boolean hasRequestMapping = method.isAnnotationPresent(RequestMapping.class);
        if (hasGetMapping) {
            return "GET";
        } else if (hasPostMapping) {
            return "POST";
        } else if (hasRequestMapping) {
//            需要所有请求参数都可以
            return "ANY";
        }
        return "UNKNOWN";
    }

    private List<MyMethodParameter> getMethodParameters(Method method) {
        Parameter[] parameters = method.getParameters();
        ArrayList<MyMethodParameter> myMethodParameters = new ArrayList<>();
        for (Parameter parameter : parameters) {
            MyMethodParameter myMethodParameter = new MyMethodParameter();
            String parameterName = parameter.getName();
            Class<?> parameterType = parameter.getType();
//            TODO 可以添加设置默认值
            myMethodParameter.setDefaultValue(null);
            myMethodParameter.setName(parameterName);
            myMethodParameter.setType(parameterType);
            myMethodParameter.setFromUrl(!parameter.isAnnotationPresent(RequestBody.class));
//            TODO 可以添加设置是否必须,后面在管
            myMethodParameter.setRequire(true);
            myMethodParameters.add(myMethodParameter);

        }
        return myMethodParameters;
    }

    private String getControllerMappingUrl(Class<?> aClass) {
        PostMapping postAnnotation = aClass.getAnnotation(PostMapping.class);
        RequestMapping requestAnnotation = aClass.getAnnotation(RequestMapping.class);
        GetMapping annotation = aClass.getAnnotation(GetMapping.class);
        if (postAnnotation != null) {
            return postAnnotation.value();
        } else if (requestAnnotation != null) {
            return requestAnnotation.value();
        } else if (annotation != null) {
            return annotation.value();
        }
        return null;
    }
    private String getMethodMappingUrl(Method method) {
        PostMapping postAnnotation = method.getAnnotation(PostMapping.class);
        RequestMapping requestAnnotation = method.getAnnotation(RequestMapping.class);
        GetMapping annotation = method.getAnnotation(GetMapping.class);
        if (postAnnotation != null) {
            return postAnnotation.value();
        } else if (requestAnnotation != null) {
            return requestAnnotation.value();
        } else if (annotation != null) {
            return annotation.value();
        }
        return null;
    }


    private List<Method> getControllerMappingMethod(Class<?> aClass) {
        List<Method> controllerMethods = new ArrayList<>();
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            for (Annotation declaredAnnotation : declaredAnnotations) {
//                判断是否是requestMapping或getMapping或PostMapping
                if (method.isAnnotationPresent(GetMapping.class) || method.isAnnotationPresent(PostMapping.class) || method.isAnnotationPresent(RequestMapping.class)) {
                    controllerMethods.add(method);
                }

            }
        }
        return controllerMethods;
    }


}

