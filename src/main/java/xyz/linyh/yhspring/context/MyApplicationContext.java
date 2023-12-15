package xyz.linyh.yhspring.context;

import xyz.linyh.yhspring.annotation.YhAutoWrite;
import xyz.linyh.yhspring.annotationScan.ComponentScan;
import xyz.linyh.yhspring.entity.BeanDefinition;
import xyz.linyh.yhspring.utils.ScanUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义的ApplicationContext
 *
 * 存一些实例化后的bean对象
 * ...
 *
 * @author lin
 */
public class MyApplicationContext {


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
        return MyApplicationContextHolder.instance;
    }

    private MyApplicationContext() {
//        初始化
        beans = new HashMap<>();
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
//        TODO 还需要将configuration里面带有bean的注入到ioc容器中

//        将注册到bean容器里面的类在扫描一遍，看有没有携带@autowrite这些注解的，在注入一遍
        beans.forEach((k, v) -> {
            Class<?> beanClass = v.getBeanClass();
            for (Field field : beanClass.getDeclaredFields()) {
                if (needAutowired(field)) {
                    Class<? extends Field> aClass = field.getClass();
                    System.out.println(field.getName());
                    System.out.println(field.getType());
//                    TODO 只是根据参数名字注入而已，还需要判断类型（判断在bean容量里面的对象，如果是这个属性的实现类或父类，那么也可以注入）
                    BeanDefinition beanDefinition = beans.get(field.getName());

                    if(beanDefinition == null){
                        System.out.println(field.getName()+":这个属性没有注入成功");
                    }
                    Object instance = beanDefinition.getInstance();
                    try {
                        field.setAccessible(true);
                        field.set(v.getInstance(), instance);
                    } catch (IllegalAccessException e) {
                        System.out.println("属性注入失败");
                        e.printStackTrace();
                    }
                }
            }
        });

        // TODO 后面所有加入到beans后，这个context可能也要加到ioc容器中
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
