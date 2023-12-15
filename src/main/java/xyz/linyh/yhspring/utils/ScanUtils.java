package xyz.linyh.yhspring.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具类，可以用来扫描包下的所有类，判断是否有是对应属性的
 *
 * @author lin
 */
public class ScanUtils {


    /**
     * 根据类路径，获取携带annotations的类
     *
     * @param dir
     * @param filePath
     * @param classes
     * @return
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> getClassByUrl(File dir, String filePath, ArrayList<Class<?>> classes, List<Class> annotations) throws ClassNotFoundException {

        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getClassByUrl(file, filePath + "." + file.getName(), classes, annotations);
            } else {
                String name = file.getName();
                if (name.endsWith(".class")) {
                    String className = name.substring(0, name.lastIndexOf("."));
                    Class<?> clazz = Class.forName(filePath + "." + className);
//                    判断是否有annotations里面的类
                    for (Class annotation : annotations) {
                        if (clazz.isAnnotationPresent(annotation)) {
                            classes.add(clazz);
                            break;
                        }
                    }

                }

            }
        }

        return classes;
    }

    /**
     * 让字符串首字母变为小写
     */
    public static String FirstWorldToLower(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char firstWord = Character.toLowerCase(name.charAt(0));
        String substring = name.substring(1);
        return firstWord + substring;

    }
}
