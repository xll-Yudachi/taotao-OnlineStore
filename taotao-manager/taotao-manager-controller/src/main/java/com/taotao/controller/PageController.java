package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageController {

	//首页
	@RequestMapping("/")
	public String showIndex() {
		return "index";
	}
	
	//其他界面
	@RequestMapping("/{page}")
	public String showpage(@PathVariable String page) {
		return page;
	}
}
