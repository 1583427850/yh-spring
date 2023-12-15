package xyz.linyh.yhspring.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import xyz.linyh.yhspring.annotation.*;
import xyz.linyh.yhspring.entity.MyMethod;
import xyz.linyh.yhspring.entity.MyMethodParameter;
import xyz.linyh.yhspring.test.service.TestService;
import xyz.linyh.yhspring.test.service.impl.TestServiceImpl;
import xyz.linyh.yhspring.test.service.impl.TestServiceImpl2;

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


    @RequestMapping(value = "/test")
    public void test1(String username, MyMethod testMethod) {
        System.out.println(username);
        System.out.println(testMethod);

    }

    @PostMapping(value = "/test2")
    public MyMethodParameter test2(String id, @RequestBody user user) {
        System.out.println(id);
        System.out.println(user);
        return null;

    }


    @GetMapping(value = "/test3")
    public void test3(String username, MyMethod testMethod) {

    }

    @GetMapping(value="/session1")
    public void session1(HttpServletRequest request){
        request.getSession().setAttribute("name","lin");
        System.out.println("ok");
    }

    @GetMapping(value="/session2")
    public void session2(HttpServletRequest request){
        System.out.println(request.getSession().getAttribute("name"));
        System.out.println("ok");
    }

    @PostMapping("/test4/{id}/{pageId}")
    public user test4(String username, @RequestBody user user, @PathVariable("id") String id, @PathVariable("pageId") String pageId, HttpServletRequest request) {
        System.out.println(username);
        System.out.println(user);
        System.out.println(id);
        System.out.println(pageId);
        test2Service.hello();
        testService.say();
        return user;
    }

    @Data
    class user {
        private String username;
        private String password;
    }


    public static void main(String[] args) {
//        判断某一个类能否被另一个类赋值 test = new 判断某一个类能否被另一个类赋值();
        Class<?> testServiceImpl1Class = TestServiceImpl.class;
        Class<?> testServiceImpl2Class = TestServiceImpl2.class;

        System.out.println(testServiceImpl1Class.isAssignableFrom(testServiceImpl2Class));
        Class<TestService> testServiceClass = TestService.class;
        System.out.println(testServiceClass.isAssignableFrom(testServiceImpl1Class));

        if(HttpResponse.class.isAssignableFrom(HttpServletRequest.class) || HttpResponse.class.isAssignableFrom(HttpRequest.class)) {
            System.out.println("true");
        }
//            methodParams.add(request);
        }



    }



