package xyz.linyh.yhspring.test;

import xyz.linyh.yhspring.annotation.PostMapping;
import xyz.linyh.yhspring.annotation.RequestMapping;
import xyz.linyh.yhspring.annotation.YhController;
import xyz.linyh.yhspring.entity.MyMethod;
import xyz.linyh.yhspring.entity.MyMethodParameter;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@YhController
public class Test {

    @RequestMapping(value = "/test")
    public void test1(String username, MyMethod testMethod){

    }

    @PostMapping(value = "/test2")
    public  MyMethodParameter test2(MyMethodParameter params){
        return null;

    }


}
