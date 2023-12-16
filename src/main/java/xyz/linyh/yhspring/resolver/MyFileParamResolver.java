package xyz.linyh.yhspring.resolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import xyz.linyh.yhspring.constant.RequestConstant;
import xyz.linyh.yhspring.entity.MultiPartFile;
import xyz.linyh.yhspring.entity.MyMethodParameter;

import java.io.*;
import java.util.Collection;

/**
 * TODO 可以写在一个配置文件里面，启动的时候全部一起读出来然后放到容器中
 */
public class MyFileParamResolver implements HandlerMethodArgumentResolver {
    /**
     * 查看是否支持这个参数解析
     *
     * @param parameter
     * @return
     */
    @Override
    public boolean supports(MyMethodParameter parameter) {
        return parameter.getParamType().equals(RequestConstant.PARAM_TYPE_MULTIPART);
    }

    /**
     * TODO 简易版
     * 解析出request里面的参数内容
     *
     * @param parameter
     * @param request
     * @return
     */
    @Override
    public Object resolveArgument(MyMethodParameter parameter, HttpServletRequest request) throws IOException {
//        判断请求格式是否正确
        String contentType = request.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("multipart/form-data")) {
            return null;
        }

//        解析
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        FileItemIterator itemIterator = upload.getItemIterator(request);

//        TODO 目前先只能解析一个
        while(itemIterator.hasNext()){
            FileItemStream next = itemIterator.next();
            InputStream inputStream = next.openStream();
            MultiPartFile multiPartFile = new MultiPartFile();
            multiPartFile.setFileName(next.getName());
            multiPartFile.setInputStream(inputStream);

            return multiPartFile;
        }
        return null;
    }
}