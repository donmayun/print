package netty.http.mvc;

import netty.http.ServerConfig;
import netty.http.handler.DispatchHandler;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxw on 2016/1/13.
 */
public class HttpVelocityManager {

    private static final String suffix = ".vm";

    static VelocityEngine ve;

    static{
        ve = new VelocityEngine();
        ve.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, ServerConfig.getResourcePath() + DispatchHandler.resourceUrl + "/WEB-INF/html/");
        ve.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_CACHE, false);
        ve.setProperty(VelocityEngine.RESOURCE_LOADER, "file");
        ve.init();
    }

    private static Template getTemplate(String template){
        return ve.getTemplate(template, "UTF-8");
    }

    private static String merge(Template t, Map<String, Object> data){
        VelocityContext ctx = new VelocityContext();
        Map<String, Object> m = new HashMap<>();
        m.put("contextPath", "");
        ctx.put("rc", m);

        if(data != null){
            for(String key : data.keySet()){
                ctx.put(key, data.get(key));
            }
        }
        StringWriter sw = new StringWriter();
        t.merge(ctx, sw);
        String result = sw.toString();
        try {
            sw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getView(String template, Map<String, Object> data){
        return merge(getTemplate(template + suffix), data);
    }

}
