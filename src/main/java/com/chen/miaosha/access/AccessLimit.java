package com.chen.miaosha.access;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *
 * 这个注解用来进行接口防刷和登陆判断
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
	//多少秒之内不能调用多少次
	//接口防刷
	int seconds();//多少秒
	int maxCount();//多少次

	boolean needLogin() default true;//是否需要登录
}
