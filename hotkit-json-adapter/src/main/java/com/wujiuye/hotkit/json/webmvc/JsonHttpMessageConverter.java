package com.wujiuye.hotkit.json.webmvc;

import com.fasterxml.jackson.core.JsonEncoding;
import com.wujiuye.hotkit.json.JsonUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义spring mvc框架消息转换器，统一json序列化和反序列化格式
 *
 * @author wujiuye 2020/08/15
 */
public class JsonHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    private final static List<MediaType> SUPPOR_MEDIA_TYPE;

    static {
        SUPPOR_MEDIA_TYPE = Arrays.asList(
                MediaType.APPLICATION_JSON,
                // actuator框架使用
                MediaType.valueOf("application/vnd.spring-boot.actuator.v3+json"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return String.class != clazz;
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return canRead(mediaType);
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return canRead(mediaType);
    }

    @Override
    protected boolean canRead(MediaType mediaType) {
        return super.canRead(mediaType);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return SUPPOR_MEDIA_TYPE;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return canWrite(mediaType);
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return canWrite(mediaType);
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return super.canRead(mediaType);
    }

    /**
     * 序列化
     *
     * @param object            对象
     * @param type              类型
     * @param httpOutputMessage 输出流
     * @throws IOException
     * @throws HttpMessageNotWritableException
     */
    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        MediaType contentType = httpOutputMessage.getHeaders().getContentType();
        JsonEncoding encoding = this.getJsonEncoding(contentType);
        String json = object instanceof String ? (String) object : JsonUtils.toJsonString(object);
        OutputStream outputStream = httpOutputMessage.getBody();
        outputStream.write(json.getBytes(encoding.getJavaName()));
        outputStream.flush();
    }

    @Override
    protected Object readInternal(Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream inputStream = httpInputMessage.getBody();
        return JsonUtils.fromJson(inputStream, aClass);
    }

    /**
     * 反序列化
     *
     * @param type             参数类型
     * @param aClass           Controller的类型
     * @param httpInputMessage 输入流
     * @return
     * @throws IOException
     * @throws HttpMessageNotReadableException
     */
    @Override
    public Object read(Type type, Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream inputStream = httpInputMessage.getBody();
//      String json = readMessageBody(inputStream);
//      return JsonUtils.fromJson(json, type);
        return JsonUtils.fromJson(inputStream, type);
    }

    private String readMessageBody(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    protected JsonEncoding getJsonEncoding(@Nullable MediaType contentType) {
        if (contentType != null && contentType.getCharset() != null) {
            Charset charset = contentType.getCharset();
            JsonEncoding[] encodings = JsonEncoding.values();
            for (JsonEncoding encoding : encodings) {
                if (charset.name().equals(encoding.getJavaName())) {
                    return encoding;
                }
            }
        }
        return JsonEncoding.UTF8;
    }

}
