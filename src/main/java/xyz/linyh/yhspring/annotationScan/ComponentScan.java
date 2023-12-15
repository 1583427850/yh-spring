package xyz.linyh.yhspring.annotationScan;

import lombok.extern.slf4j.Slf4j;
import xyz.linyh.yhspring.annotation.Configuration;
import xyz.linyh.yhspring.annotation.YhComponent;
import xyz.linyh.yhspring.annotation.YhController;
import xyz.linyh.yhspring.annotation.YhService;
import xyz.linyh.yhspring.utils.ScanUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用来扫描所有需要注册到ioc容器的所有类
 *
 * @author lin
 */
@Slf4j
public class ComponentScan {

    /**
     * 扫描所有需要注册到容器的注解的类
     *
     * @param packageName
     * @return
     */
    public static List<Class<?>> getComponentClass(String packageName) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ArrayList<Class<?>> classes = new ArrayList<>();
        URL resource = contextClassLoader.getResource(packageName.replace(".", "/"));
        if (resource == null) {
            return classes;
        }
        File dir = new File(resource.getFile());
        try {
            return ScanUtils.getClassByUrl(dir, packageName, classes, Arrays.asList(YhController.class, YhService.class, YhComponent.class, Configuration.class));
        } catch (ClassNotFoundException e) {
            System.out.println("扫描类失败");
//            TODO 改为打印日志
            log.error("扫描类失败,{}", e.getMessage());
            return null;
        }

    }

}
