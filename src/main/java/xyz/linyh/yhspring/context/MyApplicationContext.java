package xyz.linyh.yhspring.context;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import xyz.linyh.yhspring.annotation.YhAutoWrite;
import xyz.linyh.yhspring.annotation.YhValue;
import xyz.linyh.yhspring.annotationScan.ComponentScan;
import xyz.linyh.yhspring.entity.BeanDefinition;
import xyz.linyh.yhspring.utils.ScanUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义的ApplicationContext
 * 存一些实例化后的bean对象
 * ...
 *
 * @author lin
 */
@Slf4j
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

//        将注册到bean容器里面的类在扫描一遍，看有没有携带@autowrite这些注解的，如果有，就注入
        beans.forEach((k, v) -> {
            Class<?> beanClass = v.getBeanClass();
            for (Field field : beanClass.getDeclaredFields()) {
                if (needAutowired(field)) {

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
                } else if (field.isAnnotationPresent(YhValue.class)) {
//                    TODO 读取配置文件，然后写入值
                    Yaml yaml = new Yaml();
                    URI uri = null;
                    try {
//                        TODO 修改格式和逻辑
                        uri = ClassLoader.getSystemResource("application.yml").toURI();
                        InputStream in = Files.newInputStream(Paths.get(uri));
                        Map map = yaml.loadAs(in, Map.class);
                        in.close();

                        map.forEach((k2, v2) -> {
                            System.out.println(k2 + "  " + v2);
                        });
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });

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
