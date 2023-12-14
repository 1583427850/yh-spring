package xyz.linyh.yhspring.servlet;

import lombok.Data;
import xyz.linyh.yhspring.entity.MyMethod;

@Data
public class YhHandlerExecutionChain {

    private MyMethod handlerMethod;

//        TODO 还有一些interfaceteor过滤器链

    public YhHandlerExecutionChain(MyMethod handler) {
        this.handlerMethod = handler;
    }
}
