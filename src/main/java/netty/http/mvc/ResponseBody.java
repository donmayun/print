package netty.http.mvc;

import java.lang.annotation.*;

/**
 * Created by zxw on 2016/1/13.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseBody {

}
