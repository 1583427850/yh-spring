package xyz.linyh.yhspring.test.config;

import lombok.Value;
import xyz.linyh.yhspring.annotation.Configuration;
import xyz.linyh.yhspring.annotation.YhValue;

/**
 * @author lin
 */
@Configuration
public class TestConfig {

    @YhValue("test.name")
    private String name;
}
