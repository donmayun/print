package netty.http.mvc;

import java.lang.annotation.*;

/**
 * Created by Don on 2016/1/13.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseBody {

}
