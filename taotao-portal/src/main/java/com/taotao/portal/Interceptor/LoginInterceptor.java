package com.taotao.portal.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.Impl.UserServiceImpl;

public class LoginInterceptor implements HandlerInterceptor{

	@Autowired
	private UserServiceImpl userService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		//在返回modelandview之后处理
		//响应用户之后
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		//在handler执行之后 返回modelandview之前处理
		
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		//在handler执行之前处理
		//返回值决定handler是否执行
		
		//从cookie中取token
		String token = CookieUtils.getCookieValue(arg0, "TT_TOKEN");
		//根据token换取用户信息,调用sso系统的接口
		TbUser user = userService.getUserByToken(token);
		//取不到用户信息
		if(user == null) {
			//跳转到登录界面,把用户请求的url作为参数传递给登录界面
			arg1.sendRedirect(userService.SSO_BASE_URL + userService.SSO_PAGE_LOGIN + "?redirect=" + arg0.getRequestURL());
			//返回false
			return false;
		}
		//取到用户信息,放行
		//把用户信息放入Request中
		/*
		 SpringMVC框架中 通过request请求到处理器映射器去换取handle,返回一个HandlerExecutionChain对象,
		 而在HandlerExecutionChain中包含了handle对象和request对象,在返回HandlerExecutionChain之前
		 添加执行了Interceptor拦截器。整个过程类似于责任链模式,request->Interceptor->HandlerExecutionChain->Handle
		 源码地址 https://www.cnblogs.com/my-123/p/8617391.html
		*/
		arg0.setAttribute("user", user);
		return true;
	}
}
