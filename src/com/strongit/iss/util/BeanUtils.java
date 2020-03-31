package com.strongit.iss.util;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

public class BeanUtils {
	protected static final Log logger = LogFactory.getLog(BeanUtils.class);

	public static Object getFieldValue(Object object, String fieldName)
			throws NoSuchFieldException {
		Field field = getDeclaredField(object, fieldName);
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			if (logger.isErrorEnabled()) {
				logger.error("不可能抛出的异常{}", e);
			}
		}
		return result;
	}

	public static void setFieldValue(Object object, String fieldName,
			Object value) throws NoSuchFieldException {
		Field field = getDeclaredField(object, fieldName);
		if (!field.isAccessible())
			field.setAccessible(true);
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			if (logger.isErrorEnabled())
				logger.error("不可能抛出的异常:{}", e);
		}
	}

	public static Field getDeclaredField(Object object, String fieldName)
			throws NoSuchFieldException {
		Assert.notNull(object);
		return getDeclaredField(object.getClass(), fieldName);
	}

	public static Field getDeclaredField(Class clazz, String fieldName)
			throws NoSuchFieldException {
		Assert.notNull(clazz);
		Assert.hasText(fieldName);
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass())
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
			}
		throw new NoSuchFieldException("没有此字段: " + clazz.getName() + '.'
				+ fieldName);
	}
}