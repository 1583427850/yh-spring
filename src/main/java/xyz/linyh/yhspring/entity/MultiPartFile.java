package xyz.linyh.yhspring.entity;

import lombok.Data;

import java.io.InputStream;

/**
 * 如果参数列表要传输文件或什么，可以用这个类
 */
@Data
public class MultiPartFile {

    private String fileName;

    private InputStream inputStream;
}
