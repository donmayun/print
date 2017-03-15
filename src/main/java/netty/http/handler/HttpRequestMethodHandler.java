package netty.http.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.util.CharsetUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import netty.http.Filter;
import netty.http.ServerContextManager;
import netty.http.context.HttpMethodRequest;
import netty.http.context.HttpMethodResponse;
import netty.http.mvc.HttpVelocityManager;
import netty.http.mvc.ModelMap;
import netty.http.mvc.RequestMapping;
import netty.http.mvc.ResponseBody;
import netty.http.utils.Classes;
import netty.http.utils.SpringContext;
import netty.http.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.ReflectionUtils;

/**
 * Created by Don on 2016/1/13.
 */
public class HttpRequestMethodHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private PathMatcher pathMatcher = new AntPathMatcher();

    public Map<String, MapMethod> mapMethod = new HashMap<>();

    public HttpRequestMethodHandler(){
        init();
    }

    public void handle(FullHttpRequest request, FullHttpResponse response, Map<String, Object> session, ChannelHandlerContext ctx) throws Exception {
        HttpMethodRequest req = new HttpMethodRequest(request, session);

        MapMethod mm = mapMethod.get(req.getPath());
        if(mm == null){
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        HttpMethodResponse resp = new HttpMethodResponse();

        ModelMap modelMap = new ModelMap();
        boolean flag = true;
        for(Filter filter : mm.getFilters()){
            flag = filter.before(ctx, req, resp);
            if(!flag){
                break;
            }
        }
        if(flag) {
            try {
                Object obj = invokeMethod(mm, req, resp, modelMap);
                for(FileUpload fu : req.getFileUploadMap().values()){
                    fu.getFile().delete();
                }
                for (Filter filter : mm.getFilters()) {
                    filter.after(ctx, req, resp);
                }
                if (mm.isResponseBody()) {
                    resp.clear();
                    if (obj != null) {
                        if (obj instanceof String) {
                            resp.write(((String) obj).getBytes(Charset.forName("UTF-8")));
                        } else {
                            String str = StringUtils.toJson(obj);
                            if (str != null) {
                                resp.write(str.getBytes(Charset.forName("UTF-8")));
                            }
                        }
                    }
                } else {
                    if (obj != null && obj instanceof String) {
                        //跳转到页面
                        try {
                            String body = HttpVelocityManager.getView((String) obj, modelMap.getMap());
                            if (body != null) {
                                resp.write(body.getBytes(Charset.forName("UTF-8")));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
                            return;
                        }
                    } else {
                        if (obj != null) {
                            String str = StringUtils.toJson(obj);
                            if (str != null) {
                                resp.write(str.getBytes(Charset.forName("UTF-8")));
                            }
                        }
                    }
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                throw e;
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

        HttpHeaders.setContentLength(response, resp.getContentLength());
        response.headers().set(HttpHeaders.Names.CONTENT_LANGUAGE, "zh-CN");
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html;charset=UTF-8");
        response.headers().set(HttpHeaders.Names.TRANSFER_ENCODING, HttpHeaders.Values.CHUNKED);
        response.headers().set(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        response.headers().set(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_METHODS,"GET");
        response.headers().set(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_HEADERS,"x-requested-with,content-type");
        if (HttpHeaders.isKeepAlive(request)) {
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        response.setStatus(resp.getStatus());
        response.content().writeBytes(resp.getByteBuf());
        ctx.writeAndFlush(response);
    }

    public void init(){
        List<Filter> filters = ServerContextManager.getInstance().getFilters();
        Map<String, Object> map = SpringContext.getApplicationContext()
                .getBeansWithAnnotation(Controller.class);
        for (final Object obj : map.values()) {
            RequestMapping mapping = AnnotationUtils.findAnnotation(
                    obj.getClass(), RequestMapping.class);
            String temp = null;
            if(mapping != null){
                temp = mapping.value();
            }
            final String prefix = temp;
            ReflectionUtils.doWithMethods(obj.getClass(),
                    new ReflectionUtils.MethodCallback() {
                        public void doWith(Method method) {
                            RequestMapping mapping = AnnotationUtils
                                    .findAnnotation(method,
                                            RequestMapping.class);
                            ResponseBody responseBody = AnnotationUtils
                                    .findAnnotation(method,
                                            ResponseBody.class);
                            if (mapping != null) {
                                String url = null;
                                if (prefix == null) {
                                    url = mapping.value();
                                } else {
                                    url = prefix + mapping.value();
                                }
                                if (mapMethod.containsKey(url)) {
                                    throw new RuntimeException("url 已存在");
                                } else {
                                    mapMethod.put(url, new MapMethod(obj, method, responseBody != null));
                                }
                            }
                        }
                    }, ReflectionUtils.USER_DECLARED_METHODS);
        }
        for(String key:mapMethod.keySet()){
            for(Filter filter: filters) {
                if(pathMatcher.match(filter.pattern(), key)){
                    mapMethod.get(key).getFilters().add(filter);
                }
            }
            logger.info("url : [{}], method : [{}]", key, mapMethod.get(key).getMethod().getDeclaringClass() + "." + mapMethod.get(key).getMethod().getName() + "()");
        }
    }


    private Object invokeMethod(MapMethod mm, HttpMethodRequest request, HttpMethodResponse response, ModelMap modelMap) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Class[] classes = mm.getMethod().getParameterTypes();
        if(classes == null || classes.length == 0){
            return mm.getMethod().invoke(mm.getObj());
        }else {
            String [] names = Classes.getMethodParamNames(mm.getMethod());
            Object [] params = new Object[names.length];
            for(int i=0; i<params.length; i++){
                if(classes[i] == HttpMethodRequest.class){
                    params[i] = request;
                }else if(classes[i] == HttpMethodResponse.class){
                    params[i] = response;
                }else if(classes[i] == ModelMap.class){
                    params[i] = modelMap;
                }else if(classes[i] == FileUpload.class){
                    String name = names[i];
                    FileUpload fu = request.getFileUploadMap().get(name);
                    if(fu != null){
                        params[i] = fu;
                    }
                }else {
                    Class type = classes[i];
                    String value = request.getParameter(names[i]);
                    if (type == Boolean.TYPE || type == Boolean.class
                            || type == Character.TYPE || type == Character.class
                            || type == Byte.TYPE || type == Byte.class
                            || type == Short.TYPE || type == Short.class
                            || type == Integer.TYPE || type == Integer.class
                            || type == Long.TYPE || type == Long.class
                            || type == Float.TYPE || type == Float.class
                            || type == Double.TYPE || type == Double.class
                            || type == String.class) {
                        params[i] = Classes.getValue(value, type);
                    }else{
                        Field[] fields = classes[i].getDeclaredFields();
                        Object obj = classes[i].newInstance();
                        if(fields!= null) {
                            for (Field field : fields){
                                String val = request.getParameter(field.getName());
                                if(val != null) {
                                    field.setAccessible(true);
                                    field.set(obj, Classes.getValue(val, field.getType()));
                                    field.setAccessible(false);
                                }
                            }
                        }
                        params[i] = obj;
                    }
                }
            }
            return mm.getMethod().invoke(mm.getObj(), params);
        }
    }

    public static class MapMethod{
        private Object obj;
        private Method method;
        private boolean responseBody;
        private List<Filter> filters = new ArrayList<>();

        public MapMethod() {
        }

        public MapMethod(Object obj, Method method, boolean responseBody) {
            this.obj = obj;
            this.method = method;
            this.responseBody = responseBody;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public boolean isResponseBody() {
            return responseBody;
        }

        public void setResponseBody(boolean responseBody) {
            this.responseBody = responseBody;
        }

        public List<Filter> getFilters() {
            return filters;
        }

        public void setFilters(List<Filter> filters) {
            this.filters = filters;
        }
    }

    private static void sendError(ChannelHandlerContext ctx,
                                  HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status, Unpooled.copiedBuffer("Failure: " + status.toString()
                + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
