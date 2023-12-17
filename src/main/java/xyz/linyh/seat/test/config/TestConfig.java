package xyz.linyh.seat.test.config;

import lombok.Data;
import xyz.linyh.yhspring.annotation.YhBean;
import xyz.linyh.yhspring.annotation.YhConfiguration;
import xyz.linyh.yhspring.annotation.YhValue;
import xyz.linyh.seat.test.eneity.Prople;

/**
 * @author lin
 */
@YhConfiguration
@Data
public class TestConfig {

    @YhValue("test.name")
    private String name;

    @YhValue("test.age")
    private Integer age;

    @YhBean
    public Prople prople() {
        Prople prople = new Prople();
        prople.setAge(age);
        prople.setName(name);
        return prople;
    }
}
