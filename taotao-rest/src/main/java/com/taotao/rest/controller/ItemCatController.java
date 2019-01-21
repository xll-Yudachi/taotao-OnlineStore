package com.taotao.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.utils.JsonUtils;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;

/**
 * 
 * @ClassName ItemCatController
 * @Description 商品分类列表 
 * @Author xll
 * @Date 2018年12月27日 下午7:05:15
 *
 */
@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	
	//第一种方法:字符串拼接
	/*@RequestMapping(value="/itemcat/list",produces=MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	@ResponseBody
	public String getItemCatList(String callback) {
		CatResult catResult = itemCatService.getItemCatList();
		//把pojo转换成字符串
		String json = JsonUtils.objectToJson(catResult);
		//拼装返回值
		String result = callback + "(" + json + ")";
		
		return result;
	}*/
	
	//第二种方法:springmvc框架的封装类
	@RequestMapping("/itemcat/list")
	@ResponseBody
	public Object getItemCatList(String callback) {
		CatResult catResult = itemCatService.getItemCatList();
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(catResult);
		mappingJacksonValue.setJsonpFunction(callback);
		return mappingJacksonValue;
	}
}
