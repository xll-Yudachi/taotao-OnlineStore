package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String param,@PathVariable Integer type,String callback) {
		
		TaotaoResult result = null;

		//参数有效性校验
		if(StringUtils.isBlank(param)) {
			result = TaotaoResult.build(400, "校验内容不能为空");
		}
		if(type == null) {
			result = TaotaoResult.build(400, "校验内容类型不能为空");
		}
		if(type != 1 && type != 2 && type != 3) {
			result = TaotaoResult.build(400, "校验内容类型错误");
		}
		
		//校验出错
		if(result != null) {
			if(callback != null) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			}else {
				return result;
			}
		}
		
		//参数有效 调用服务
		try {
			result = userService.checkData(param, type);
		} catch (Exception e) {
			e.printStackTrace();
			result = TaotaoResult.build(500, e.getMessage());
		}
		
		if(callback != null) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}else {
			return result;
		}
	}
	
	/**
	 * 创建用户
	 */
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createUser(TbUser tbUser) {
		try {
			return userService.createUser(tbUser);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, e.getMessage());
		}
	}
	
	/**
	 * 用户登录
	 */
	@RequestMapping(value = "/user/login", method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult userLogin(String username,String password,HttpServletRequest request,HttpServletResponse response) {
		try {
			return userService.userLogin(username, password,request,response);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, e.getMessage());
		}
	}
	
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback) {
		
		TaotaoResult taotaoResult = null;
		
		try {
			taotaoResult = userService.getUserByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			taotaoResult = TaotaoResult.build(500, e.getMessage());
		}
		
		if(callback!=null) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(taotaoResult);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return taotaoResult;
	}
}
