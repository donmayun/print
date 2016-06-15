package netty.http.utils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by zxw on 1/7/16.
 */
public class VelocityManager {

    public static final String CONF_VM = "vm/conf_temp.vm";
    public static final String ENV_SH = "vm/hadoop-env.sh";

    static VelocityEngine ve;

    static{
        ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

        ve.init();
    }

    private static Template getTemplate(String template){
        return ve.getTemplate(template);
    }

    private static String merge(Template t, Map<String, Object> data){
        VelocityContext ctx = new VelocityContext();

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
        return merge(getTemplate(template), data);
    }

}
