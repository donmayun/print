package netty.http.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Created with IntelliJ IDEA. User: Don Date: 13-6-18 Time: 下午4:57 To change
 * this template use File | Settings | File Templates.
 */
public class SpringContext {

	private static Logger logger = LoggerFactory.getLogger(SpringContext.class);

	private static ApplicationContext ac;

	public static void setApplicationContext(ApplicationContext ac) {
		SpringContext.ac = ac;
	}

	public static ApplicationContext getApplicationContext() {
		return ac;
	}

	public static Object getBean(String name) {
		if (ac == null) {
			logger.warn("applicationContext is null");
			return null;
		}
		return ac.getBean(name);
	}

	public static Object getBeanNoException(String name) {
		try {
			return getBean(name);
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> T getBean(Class<T> clazz) {
		if (ac == null) {
			logger.warn("applicationContext is null");
			return null;
		}
		return ac.getBean(clazz);
	}

}
