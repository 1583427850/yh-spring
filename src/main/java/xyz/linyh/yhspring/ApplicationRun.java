package xyz.linyh.yhspring;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import xyz.linyh.yhspring.annotationScan.ControllerAnnoScan;
import xyz.linyh.yhspring.entity.MyMethod;
import xyz.linyh.yhspring.handle.HandlerMapping;
import xyz.linyh.yhspring.handle.SimpleHandlerMapping;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class ApplicationRun {

    private static List<Class<?>> controllerClass;

    public static void run(String... args) throws IOException, ClassNotFoundException, LifecycleException {

//        获取所有的bean环境
//        获取这个类当前的package
        String packageName = ApplicationRun.class.getPackage().getName();
        controllerClass = ControllerAnnoScan.getControllerClass(packageName);
//        根据controller里面的所有controller，获取所有的方法
        SimpleHandlerMapping simpleHandlerMapping = new SimpleHandlerMapping();
        List list = simpleHandlerMapping.buildMapping(controllerClass);

//        启动tomcat
        startTomcat();

        for (Object o : list) {
            System.out.println(o);
        }

    }

    private static void startTomcat() throws LifecycleException {
//        TODO 可能还要获取配置文件
                Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port", 8080));
        tomcat.getConnector();
        // 创建webapp:
        Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(
                new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
        ctx.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();


    }

    public static void main(String[] args) throws Exception {
        run();
    }


}
