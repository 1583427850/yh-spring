package xyz.linyh.yhspring.annotationScan;

import lombok.extern.slf4j.Slf4j;
import xyz.linyh.yhspring.annotation.YhController;
import xyz.linyh.yhspring.utils.ScanUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 可以用来扫描某一个包下面所有controller的类
 *
 * @author lin
 */
@Slf4j
public class ControllerAnnoScan {


    public static List<Class<?>> getControllerClass(String packageName) throws IOException, ClassNotFoundException {

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ArrayList<Class<?>> classes = new ArrayList<>();
        URL resource = contextClassLoader.getResource(packageName.replace(".", "/"));
        if (resource == null) {
            return classes;
        }
        File dir = new File(resource.getFile());
        return ScanUtils.getClassByUrl(dir, packageName, classes, Arrays.asList(YhController.class));

//        return getClassByUrl(dir, packageName, classes);
    }


//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        List<Class<?>> controllerClass = getControllerClass("xyz.linyh.yhspring");
//        System.out.println(controllerClass);
//    }
}
