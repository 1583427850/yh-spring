package xyz.linyh.yhspring.handle;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import xyz.linyh.yhspring.annotation.*;
import xyz.linyh.yhspring.constant.RequestConstant;
import xyz.linyh.yhspring.entity.MyMethod;
import xyz.linyh.yhspring.entity.MyMethodParameter;
import xyz.linyh.yhspring.entity.RouterNode;
import xyz.linyh.yhspring.servlet.YhHandlerExecutionChain;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

/**
 * @author lin
 */
@Slf4j
public class SimpleHandlerMapping implements HandlerMapping {

    /**
     * 保存每一个controller里面的所有方法和里面的所有接口地址
     */
//    List<MyMethod> allMappingMethod = new ArrayList<>();

    RouterNode routerNodeHeader;

    private SimpleHandlerMapping() {
    }


    public RouterNode getRouterNodeHeader() {
        return routerNodeHeader;
    }

    private static class SimpleHandlerMappingHolder {
        private static final SimpleHandlerMapping INSTANCE = new SimpleHandlerMapping();
    }

    public static SimpleHandlerMapping getInstance() {
        return SimpleHandlerMappingHolder.INSTANCE;
    }


    /**
     * 传入controller，解析里面带有特定注解的方法
     *
     * @param controllerClass
     * @return
     */
    @Override
    public void buildMapping(List<Class<?>> controllerClass) {
//        List<MyMethod> myMethods = new ArrayList<>();

//        初始化路由树
        initRouterNode();

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
                String url = prefixUrl == null ? mappingUrl : prefixUrl + mappingUrl;

                myMethod.setUrl(url);
                myMethod.setMethodName(method.getName());
                myMethod.setReturnType(method.getReturnType());
                myMethod.setMethodParameters(getMethodParameters(method));
                myMethod.setRequestMethod(getRequestMethod(method));
                myMethod.setClassName(aClass);
                String[] split = url.split("/");

                split = Arrays.copyOfRange(split,1,split.length);
                buildRouterNode(split, myMethod);

            }
        }
        log.info("初始化所有接口成功。。。。。");
    }

    /**
     * 构建路由字典树
     * @param parts
     * @param myMethod
     */
    private void buildRouterNode(String[] parts, MyMethod myMethod) {


        parts = parsePattern(parts);

        addRouterNode(parts,0,myMethod,routerNodeHeader);



    }

    /**
     * 递归添加节点
     * @param parts
     * @param length
     * @param myMethod
     */
    private void addRouterNode(String[] parts, int length, MyMethod myMethod,RouterNode node) {
        if(length == parts.length){
            node.getMyMethods().add(myMethod);
            return;
        }

        String part = parts[length];
        RouterNode matchNode = findMatchNode(part,node);
//        如果没有匹配的节点，那么就自己创建一个新的节点，然后初始化里面的值
        if(matchNode == null) {
            RouterNode routerNode = new RouterNode();
            routerNode.setPart(part);
            routerNode.setChildren(new ArrayList<>());
            routerNode.setWild(part.equals("*"));
            routerNode.setMyMethods(new ArrayList<>());
            node.getChildren().add(routerNode);
            addRouterNode(parts, length + 1, myMethod,routerNode);
//        如果有匹配的节点，那么就将这个节点的方法添加进去
        }else{
            addRouterNode(parts, length + 1, myMethod,matchNode);
        }

    }

    /**
     * 解析通配符
     * @param parts
     * @return
     */
    private String[] parsePattern(String[] parts) {
        for(int i = 0; i < parts.length; i++){
            String part = parts[i];
            if(part.startsWith("{") && part.endsWith("}")){
                parts[i] = "*";
            }
        }
        return parts;
    }

    /**
     * 查找是否有匹配的节点
     * @param part
     * @return
     */
    private RouterNode findMatchNode(String part,RouterNode node) {
        List<RouterNode> children = node.getChildren();
        for (RouterNode child : children) {
            if(child.getPart().equals(part)){
                return child;
            }
        }
        return null;
    }

    private void initRouterNode() {
        RouterNode routerNode = new RouterNode();
        routerNode.setPart("/");
        routerNode.setChildren(new ArrayList<>());
        routerNode.setWild(false);
        routerNode.setMyMethods(new ArrayList<>());
        routerNodeHeader = routerNode;
    }


    /**
     * 根据用户请求的地址，获取对应的可以处理他的handlerMapping
     *
     * @param request
     * @return
     */
    @Override
    public YhHandlerExecutionChain getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        if (split.length>=1){
            split = Arrays.copyOfRange(split,1,split.length);
        }

//        匹配是否有匹配的路径
        RouterNode tempNode = routerNodeHeader;
        int len = 0;
        for (String s : split) {
            List<RouterNode> children = tempNode.getChildren();
            for (RouterNode child : children) {
                if(child.getPart().equals(s) || child.isWild()){
                    tempNode = child;
                    len++;
                    break;
                }
            }
        }

        if(len != split.length){
            return null;
        }

        List<MyMethod> myMethods = tempNode.getMyMethods();
        for (MyMethod myMethod : myMethods) {
            if(myMethod.getRequestMethod().equals(request.getMethod())){
                return new YhHandlerExecutionChain(myMethod);
            }
        }
        return null;

    }



    private String getRequestMethod(Method method) {
//        TODO 后面扩展为可以设置多个请求方式
        boolean hasGetMapping = method.isAnnotationPresent(GetMapping.class);
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
            String type = getParamType(parameter);
            myMethodParameter.setParamType(type);
//            TODO 可以添加设置是否必须,后面在管
            myMethodParameter.setRequire(true);
            myMethodParameters.add(myMethodParameter);

        }
        return myMethodParameters;
    }

    /**
     * 获取这个参数应该是在请求的哪个地方获取（如url参数，或body参数这些）
     *
     * @param parameter
     * @return
     */
    private String getParamType(Parameter parameter) {
        Annotation[] annotations = parameter.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof RequestBody) {
                return RequestConstant.PARAM_TYPE_BODY;
            } else if (annotation instanceof PathVariable) {
                return RequestConstant.PARAM_TYPE_PATH;
            }
        }
//        如果是httpServletRequest或response这些，还需要添加特殊的类型
        if(parameter.getType().isAssignableFrom(HttpServletRequest.class) || parameter.getType().isAssignableFrom(HttpRequest.class)){
            return RequestConstant.PARAM_TYPE_REQUEST;
        }

        if(parameter.getType().isAssignableFrom(HttpServletResponse.class) || parameter.getType().isAssignableFrom(HttpResponse.class)){
            return RequestConstant.PARAM_TYPE_RESPONSE;
        }

        System.out.println(parameter.getType());
        return RequestConstant.PARAM_TYPE_PARAM;
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

    /**
     * 获取方法上面的注解的值
     *
     * @param method
     * @return
     */
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

