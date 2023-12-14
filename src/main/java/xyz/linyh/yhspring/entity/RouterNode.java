package xyz.linyh.yhspring.entity;

import lombok.Data;

import java.util.List;

/**
 * 用字典树存储路由信息的每一个节点
 */
@Data
public class RouterNode {

    /**
     * 到当前节点的的路径
     */
    String part;

    /**
     * 所有子节点
     */
    List<RouterNode> children;

    /**
     * 与这个路径相匹配的所有方法
     */
    List<MyMethod> myMethods;

    /**
     * 是否是通配符
     */
    boolean isWild;
}
