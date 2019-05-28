package com.harry.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * spring工具类
 */
public class SpringUtils {
	private SpringUtils() {
	};

	static ApplicationContext ctx = null;

	static {
		try {
			ctx = ContextLoader.getCurrentWebApplicationContext();
			if (ctx == null) {
				String locations = "applicationContext.xml";
				ctx = new ClassPathXmlApplicationContext(locations);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据bean名字加载ioc容器中指定的bean
	 * 
	 * @param name
	 * @return
	 */
	public static Object loadBeanByName(String name) {
		return ctx.getBean(name);
	}

	/**
	 * 获取ioc容器中的bean
	 * 
	 * @param beanName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		if (ctx == null) {
			return null;
		}
		if (ctx.containsBeanDefinition(beanName)) {
			return (T) ctx.getBean(beanName);
		}
		return null;
	}
}
