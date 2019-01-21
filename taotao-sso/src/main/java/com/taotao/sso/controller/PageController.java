package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @ClassName PageController
 * @Description 页面跳转controller 
 * @Author xll
 * @Date 2019年1月7日 下午7:09:46
 * @Version 1.0
 *
 */

@Controller
@RequestMapping("/page")
public class PageController {
	
	/**
	 * 
	 * @Description 注册界面跳转 
	 * @Author xll
	 * @Date 2019年1月7日 下午7:33:42
	 * @return
	 *
	 */
	@RequestMapping("/register")
	public String showRegister() {
		return "register";
	}
	
	/**
	 * 
	 * @Description 登录界面跳转 
	 * @Author xll
	 * @Date 2019年1月7日 下午7:34:30
	 * @return
	 *
	 */
	@RequestMapping("/login")
	public String showLogin(String redirect, Model model) {
		model.addAttribute("redirect", redirect);
		return "login";
	}
}
