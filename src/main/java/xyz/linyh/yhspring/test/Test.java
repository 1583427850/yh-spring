package xyz.linyh.yhspring.test;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import xyz.linyh.yhspring.annotation.*;
import xyz.linyh.yhspring.entity.MyMethod;
import xyz.linyh.yhspring.entity.MyMethodParameter;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@YhController
public class Test {

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


    @Data
    class user {
        private String username;
        private String password;
    }

//    public static void main(String[] args) {
//        JSONUtil.toBean("")
//        {    "username": "tom",    "password": "123456"}
//    }


}
