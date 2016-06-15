package netty.http.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZXW on 2014/11/12.
 */
public class StringUtils {

	public static String toCamelCase(String input) {
		StringBuilder sb = new StringBuilder();
		final char delim = '_';
		char value;
		boolean capitalize = false;
		for (int i = 0; i < input.length(); ++i) {
			value = input.charAt(i);
			if (value == delim) {
				capitalize = true;
			} else if (capitalize) {
				sb.append(Character.toUpperCase(value));
				capitalize = false;
			} else {
				sb.append(value);
			}
		}

		return sb.toString();
	}

	public static String toJson(Object obj) {
		ObjectMapper om = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try {
			om.writeValue(sw, obj);
			String str = sw.toString();
			sw.close();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T toObject(String data, Class<T> clazz) {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.readValue(data, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T toObject(String data, TypeReference<T> tr) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(data, tr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String cutStr(String str, int limit) {
		if (str != null && str.length() > limit) {
			return str.substring(0, limit) + "…";
		} else {
			return str;
		}
	}

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 * 
	 * @param bean
	 *            要转化的JavaBean 对象
	 * @return 转化出来的 Map 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	public static Map<String, Object> BeanToMap(Object bean) throws IntrospectionException,
			IllegalAccessException,
			InvocationTargetException {
		Class<? extends Object> type = bean.getClass();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				}
			}
		}
		return returnMap;
	}

	/**
	 * @Descripiton string --> url编码
	 * @param str
	 *            第一位为需要转码字符，第二位为转码后编码
	 *
	 * @author don
	 * @date 2015年12月22日 下午7:41:07
	 */
	public static String URLEncode(String... str) {
		try {
			if (str == null) {
				return "";
			} else if (str.length == 1) {
				return URLEncoder.encode(str[0], "UTF-8");
			} else {
				return URLEncoder.encode(str[0], str[1]);
			}
		} catch (Exception e) {
			return "";
		}
	}

}
