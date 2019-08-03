package com.chen.miaosha.access;

import com.chen.miaosha.domain.MiaoshaUser;

public class UserContext {

	//一个请求过来是一个线程，同一个线程可以共享数据
	//ThreadLocal和本地线程绑定
	private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();//绑定在同一个线程中，后面的操作可以用到
	
	public static void setUser(MiaoshaUser user) {
		userHolder.set(user);
	}
	
	public static MiaoshaUser getUser() {
		return userHolder.get();
	}

}
