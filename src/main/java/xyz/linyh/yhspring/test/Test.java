package xyz.linyh.yhspring.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.SchemaOutputResolver;
import lombok.Data;
import xyz.linyh.yhspring.annotation.*;
import xyz.linyh.yhspring.entity.MultiPartFile;
import xyz.linyh.yhspring.entity.MyMethod;
import xyz.linyh.yhspring.entity.MyMethodParameter;
import xyz.linyh.yhspring.test.config.TestConfig;
import xyz.linyh.yhspring.test.eneity.Prople;
import xyz.linyh.yhspring.test.service.TestService;
import xyz.linyh.yhspring.test.service.impl.TestServiceImpl;
import xyz.linyh.yhspring.test.service.impl.TestServiceImpl2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 测试类
 * @author lin
 */
@YhController
public class Test {

    @YhAutoWrite
    private Test2Service test2Service;

    @YhAutoWrite
    private TestService testService;

    @YhAutoWrite
    private TestConfig testConfig;

    @YhAutoWrite
    Prople prople;


    @YhRequestMapping(value = "/test")
    public void test1(String username, MyMethod testMethod) {
        System.out.println(username);
        System.out.println(testMethod);

    }

    @YhPostMapping(value = "/test2")
    public MyMethodParameter test2(String id, @YhRequestBody user user) {
        System.out.println(id);
        System.out.println(user);
        return null;

    }


    @YhGetMapping(value = "/test3")
    public void test3(String username, MyMethod testMethod) {

    }

    @YhGetMapping(value="/session1")
    public void session1(HttpServletRequest request){
        request.getSession().setAttribute("name","lin");
        System.out.println("ok");
    }

    @YhGetMapping(value="/session2")
    public void session2(HttpServletRequest request){
        System.out.println(request.getSession().getAttribute("name"));
        System.out.println("ok");
    }

    @YhPostMapping("/test4/{id}/{pageId}")
    public user test4(String username, @YhRequestBody user user, @YhPathVariable("id") String id, @YhPathVariable("pageId") String pageId, HttpServletRequest request, MultiPartFile file) {
        System.out.println(username);
        System.out.println(user);
        System.out.println(id);
        System.out.println(pageId);
        test2Service.hello();
        testService.say();
        System.out.println(testConfig.getName());
        System.out.println(prople);
        System.out.println("------fileName:"+file.getFileName());
        return user;
    }


    @YhPostMapping("/test5/{id}")
    public void test5(String username,Long userId,HttpServletRequest request, @YhPathVariable("id") Long id, MultiPartFile file) {
        System.out.println(request.getSession());
        System.out.println(testConfig.getName());
        System.out.println(prople);
        System.out.println(username);
        System.out.println(userId);
        System.out.println(id);
        System.out.println("------fileName:"+file.getFileName());
        InputStream inputStream = file.getInputStream();
        try {
            inputStream.transferTo(new FileOutputStream(new File(file.getFileName())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    class user {
        private String username;
        private String password;
    }


    public static void main(String[] args) {
//        判断某一个类能否被另一个类赋值 test = new 判断某一个类能否被另一个类赋值();
//        Class<?> testServiceImpl1Class = TestServiceImpl.class;
//        Class<?> testServiceImpl2Class = TestServiceImpl2.class;
//
//        System.out.println(testServiceImpl1Class.isAssignableFrom(testServiceImpl2Class));
//        Class<TestService> testServiceClass = TestService.class;
//        System.out.println(testServiceClass.isAssignableFrom(testServiceImpl1Class));
//
//        if(HttpResponse.class.isAssignableFrom(HttpServletRequest.class) || HttpResponse.class.isAssignableFrom(HttpRequest.class)) {
//            System.out.println("true");
//        }
////            methodParams.add(request);

        String s = String.valueOf("sTRING");
        System.out.println(s);
    }




    }



