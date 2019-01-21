package com.taotao.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.portal.pojo.ItemInfo;
import com.taotao.portal.service.ItemService;

/**
 * 
 * @ClassName ItemController
 * @Description 商品详情页面展示 
 * @Author xll
 * @Date 2019年1月6日 下午3:42:16
 * @Version 1.0
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	public String showItem(@PathVariable long itemId,Model model) {
		ItemInfo item = itemService.getItemById(itemId);
		model.addAttribute("item", item);
		
		return "item";
	}
	
	@RequestMapping(value = "/item/desc/{itemId}",produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
	@ResponseBody
	public String getItemDesc(@PathVariable Long itemId) {
		return itemService.getItemDescById(itemId);
	}
	
	@RequestMapping(value = "/item/param/{itemId}",produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
	@ResponseBody
	public String getItemParam(@PathVariable Long itemId) {
		return itemService.getItemParamById(itemId);
	}
}
