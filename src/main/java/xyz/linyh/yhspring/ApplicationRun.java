package xyz.linyh.yhspring;


import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import xyz.linyh.yhspring.annotationScan.ControllerAnnoScan;
import xyz.linyh.yhspring.entity.MyMethod;
import xyz.linyh.yhspring.handle.HandlerMapping;
import xyz.linyh.yhspring.handle.SimpleHandlerMapping;
import xyz.linyh.yhspring.servlet.Dispatcherservlet;
import xyz.linyh.yhspring.servlet.TestServlet;

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
        HandlerMapping simpleHandlerMapping = SimpleHandlerMapping.getInstance();
        List list = simpleHandlerMapping.buildMapping(controllerClass);

//        启动tomcat
        startTomcat();

        for (Object o : list) {
            System.out.println(o);
        }

    }

    /**
     * 启动tomcat
     */
    private static void startTomcat(){
//        TODO 可能还要获取配置
        Tomcat tomcat = new Tomcat();

        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");

        //创建链接,绑定端口
        Connector connector = new Connector();
        connector.setPort(8081);

        StandardEngine standardEngine = new StandardEngine();
        standardEngine.setDefaultHost("localhost");

        StandardHost host = new StandardHost();
        host.setName("localhost");

        String contextPath = "";
        StandardContext context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        standardEngine.addChild(host);

        service.setContainer(standardEngine);
        service.addConnector(connector);
        tomcat.addServlet(contextPath, "dispatcherServlet", new Dispatcherservlet());
        context.addServletMappingDecoded("/*","dispatcherServlet");

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        run();
    }


}