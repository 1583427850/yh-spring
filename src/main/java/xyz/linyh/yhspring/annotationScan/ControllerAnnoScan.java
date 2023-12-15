package xyz.linyh.yhspring.annotationScan;

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

    /**
     * 递归查找所有类
     *
     * @param dir
     * @param classes
     * @return
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClassByUrl(File dir, String filePath, ArrayList<Class<?>> classes) throws ClassNotFoundException {

        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getClassByUrl(file, filePath + "." + file.getName(), classes);
            } else {
                String name = file.getName();
                if (name.endsWith(".class")) {
                    String className = name.substring(0, name.lastIndexOf("."));
                    Class<?> clazz = Class.forName(filePath + "." + className);
                    if (clazz.isAnnotationPresent(YhController.class)) {
                        classes.add(clazz);
                    }
                }

            }
        }

        return classes;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        List<Class<?>> controllerClass = getControllerClass("xyz.linyh.yhspring");
        System.out.println(controllerClass);
    }
}
