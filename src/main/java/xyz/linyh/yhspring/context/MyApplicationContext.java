package xyz.linyh.yhspring.context;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import xyz.linyh.yhspring.annotation.YhAutoWrite;
import xyz.linyh.yhspring.annotation.YhBean;
import xyz.linyh.yhspring.annotation.YhConfiguration;
import xyz.linyh.yhspring.annotation.YhValue;
import xyz.linyh.yhspring.annotationScan.ComponentScan;
import xyz.linyh.yhspring.constant.RequestConstant;
import xyz.linyh.yhspring.entity.BeanDefinition;
import xyz.linyh.yhspring.utils.ScanUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义的ApplicationContext
 * 存一些实例化后的bean对象
 * ...
 *
 * @author lin
 */
@Slf4j
public class MyApplicationContext {

    private final String defaultConfigName = "config.yaml";

    /**
     * 配置文件map
     */
    private Map configMap;


    static {


    }

    /**
     * 保存所有注册到ioc容器的bean对象
     */
    Map<String, BeanDefinition> beans;


    private static class MyApplicationContextHolder {
        private static MyApplicationContext instance = new MyApplicationContext();
    }

    /**
     * 获取单例的实例对象
     *
     * @return
     */
    public static MyApplicationContext getInstance() {
//        加载配置文件 application.yml
        return MyApplicationContextHolder.instance;
    }

    private MyApplicationContext() {
//        初始化
        beans = new ConcurrentHashMap<>();
    }

    public void loadConfig() {

        Yaml yaml = new Yaml();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(defaultConfigName)) {
//            如果为空的话，那么直接报错
            if (in == null) {
                log.error("获取不到配置文件：{}", defaultConfigName);
            }
            configMap = yaml.loadAs(in, Map.class);
        } catch (IOException e) {
            log.error("获取配置文件失败:{}", e.getMessage());
            System.exit(1);
        }

    }


    /**
     * 获取bean对象的实例
     *
     * @param beanName bean的名字
     * @return
     */
    public Object getBean(String beanName) {
        Object instance = beans.get(beanName).getInstance();
        if (instance == null) {
//            TODO
        }
        return instance;
    }

    /**
     * TODO 目前如果注入相同类名的容器，会直接覆盖前面的，后面可以用Map<String,List>来存
     * 初始化所有bean
     * //创建一个ioc容器
     *
     * @return
     */
    public void refresh(String packageName) throws Exception {
//        TODO 这个应该在创建容器后，保存到容器中
        List<Class<?>> componentClasses = ComponentScan.getComponentClass(packageName);
//        创建对应的beanDefinition，然后保存起来
        for (Class<?> componentclass : componentClasses) {
            Class<?> aClass = Class.forName(componentclass.getName());
            Object instance = aClass.getDeclaredConstructor().newInstance();
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setBeanClass(componentclass);
            beanDefinition.setBeanClassName(componentclass.getName());
            String name = ScanUtils.FirstWorldToLower(componentclass.getSimpleName());
            beanDefinition.setBeanName(name);
            beanDefinition.setInstance(instance);
            beans.put(beanDefinition.getBeanName(), beanDefinition);
        }

//        将所有杯context管理的类里面的@YhValue注入值
        beans.forEach((k,v)->{
            Class<?> beanClass = v.getBeanClass();
            for (Field field : beanClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(YhValue.class)) {
                    setValue(field, v);
                }
            }
        });

//        获取@Configuration里面的@bean注解创建的实现类，加载到bean容器里面
       beans.forEach((k,v)->{
           if(v.getBeanClass().isAnnotationPresent(YhConfiguration.class)){
//                获取里面的所有bean
               for (Method method : v.getBeanClass().getMethods()) {
                   if(method.isAnnotationPresent(YhBean.class)){
                       try {
//                           TODO 可能方法有一些参数，可以查看bean有没有这些属性，然后注入后在调用
                           Object instance = method.invoke(v.getInstance());
                           addBeanToBeans(instance,method);

                       } catch (IllegalAccessException e) {
                           throw new RuntimeException(e);
                       } catch (InvocationTargetException e) {
                           throw new RuntimeException(e);
                       }
                   }
               }
           }
       });

//        将注册到bean容器里面的类在扫描一遍，看有没有携带@autowrite这些注解的，如果有，就注入
        beans.forEach((k, v) -> {
            Class<?> beanClass = v.getBeanClass();
            for (Field field : beanClass.getDeclaredFields()) {
                if (needAutowired(field)) {
                    setAutoWrited(field, beans, v);
                }
            }
        });

    }

    /**
     * 将方法的返回值instance添加到beans容器中
     * @param instance
     * @param method
     */
    private void addBeanToBeans(Object instance, Method method) {
        if(instance==null || method==null){
            return;
        }
        String name = method.getName();
        Class<?> returnType = method.getReturnType();
        if(returnType.getName().equals("void")){
            return;
        }
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanName(name);
        beanDefinition.setBeanClass(returnType);
        beanDefinition.setBeanClassName(returnType.getName());
        beanDefinition.setInstance(instance);
        beans.put(name,beanDefinition);

    }

    /**
     * 往属性注入配置文件里面的值，
     *
     * @param field
     * @param v
     */
    private void setValue(Field field, BeanDefinition v) {
//        获取YhValue里面的value值
        YhValue yhValue = field.getAnnotation(YhValue.class);
        String value = yhValue.value();
//        按照点分隔
        String[] split = value.split("\\.");
//        去map里面获取对应的配置文件信息
        Map tempMap = configMap;
        String configValue = null;
        for (int i = 0; i < split.length-1; i++) {
            Object newMap = tempMap.get(split[i]);
            tempMap = (Map<?,?>) newMap;
        }

        try {
            if (tempMap==null){
                return;
            }
            field.setAccessible(true);
            field.set(v.getInstance(), tempMap.get(split[split.length-1]));
        } catch (IllegalAccessException e) {
            log.error("通过反射赋值属性失败:{}", e.getMessage());
        }

    }

    /**
     * 往一个java容器里面的类里面的属性注入值
     *
     * @param field
     * @param beans
     */
    private void setAutoWrited(Field field, Map<String, BeanDefinition> beans, BeanDefinition v) {
        BeanDefinition beanDefinition = beans.get(field.getName());
        Class<?> type = field.getType();
        if (beanDefinition == null) {
            beanDefinition = getBeanByType(type);
        }

        if (beanDefinition == null) {
            log.error("属性注入失败,{}", field.getName());
        }
        Object instance = beanDefinition.getInstance();
        try {
            field.setAccessible(true);
            field.set(v.getInstance(), instance);
        } catch (IllegalAccessException e) {
            log.error("属性注入失败,{}", e.getMessage());
        }
    }

    /**
     * 获取这个类型的bean（先只会获取第一个）
     *
     * @param type
     * @return
     */
    private BeanDefinition getBeanByType(Class<?> type) {
        for (BeanDefinition beanDefinition : beans.values()) {
            if (type.isAssignableFrom(beanDefinition.getBeanClass())) {
                return beanDefinition;
            }
        }
        return null;
    }

    /**
     * 判断这个属性是否需要注入
     *
     * @param field
     * @return
     */
    private boolean needAutowired(Field field) {
        if (field.isAnnotationPresent(YhAutoWrite.class)) {
            return true;
        }
        return false;
    }


}
