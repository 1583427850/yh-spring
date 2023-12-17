package xyz.linyh.seat.test.service.impl;

import xyz.linyh.yhspring.annotation.YhComponent;
import xyz.linyh.seat.test.service.TestService;

/**
 * @author lin
 */
@YhComponent
public class TestServiceImpl implements TestService{
    @Override
    public void say() {
        System.out.println("hello world");
    }
}
