package xyz.linyh.seat.config;

import lombok.extern.slf4j.Slf4j;
import xyz.linyh.yhspring.annotation.YhBean;
import xyz.linyh.yhspring.annotation.YhConfiguration;
import xyz.linyh.yhspring.annotation.YhValue;

import java.sql.*;

/**
 * 数据库连接配置（将数据库连接保存bean容器中）
 */
@YhConfiguration
@Slf4j
public class DbConfig {

    @YhValue("database.name")
    private String name;

    @YhValue("database.password")
    private String password;

    @YhValue("database.database")
    private String database;

    @YhValue("database.url")
    private String url;

    @YhValue("database.driver")
    private String driver;


    /**
     * 创建连接mysql后的对象放到bean容器中
     * @return
     */
    @YhBean
    public Connection connection(){
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,name,password);
        } catch (ClassNotFoundException e) {
            log.error("找不到对应连接数据库class,{}",e.getMessage(),e);
        } catch (SQLException e) {
            log.error("{}",e.getMessage(),e);
        }
//        PreparedStatement statement = conn.prepareStatement(sql);
//        statement.setObject(1,name);
//        Statement statement = conn.createStatement();
        log.info("连接到数据库成功");
        return conn;
    }


}
