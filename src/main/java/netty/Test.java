package netty;

import netty.http.HttpServerStarter;
import netty.http.utils.SpringContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zxw on 2016/6/14.
 */
public class Test {

    public static void main(String [] args){

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
        SpringContext.setApplicationContext(applicationContext);

        new HttpServerStarter(9191).start();
    }

}
