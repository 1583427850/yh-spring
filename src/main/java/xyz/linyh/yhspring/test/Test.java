package xyz.linyh.yhspring.test;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import xyz.linyh.yhspring.annotation.*;
import xyz.linyh.yhspring.entity.MyMethod;
import xyz.linyh.yhspring.entity.MyMethodParameter;
import xyz.linyh.yhspring.utils.ScanUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@YhController
public class Test {

    @YhAutoWrite
    private Test2Service test2Service;



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

    @PostMapping("/test4/{id}/{pageId}")
    public user test4(String username, @RequestBody user user, @PathVariable("id") String id, @PathVariable("pageId") String pageId, HttpServletRequest request) {
        System.out.println(username);
        System.out.println(user);
        System.out.println(id);
        System.out.println(pageId);
        test2Service.hello();
        return user;
    }

    @Data
    class user {
        private String username;
        private String password;
    }



}
