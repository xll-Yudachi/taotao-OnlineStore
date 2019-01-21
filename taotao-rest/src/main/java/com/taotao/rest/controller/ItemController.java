package com.taotao.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.rest.service.ItemService;

/**
 * 
 * @ClassName ItemController
 * @Description 商品信息controller
 * @Author xll
 * @Date 2019年1月4日 下午7:52:42
 * @Version 1.0
 *
 */

@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/info/{itemId}")
	@ResponseBody
	public TaotaoResult getItemBaseInfo(@PathVariable Long itemId) {
		return itemService.getItemBaseInfo(itemId);
	}
	
	@RequestMapping("/item/desc/{itemId}")
	@ResponseBody
	public TaotaoResult getItemDesc(@PathVariable long itemId) {
		return itemService.getItemDesc(itemId);
	}
	
	@RequestMapping("/item/param/{itemId}")
	@ResponseBody
	public TaotaoResult getItemParam(@PathVariable long itemId) {
		return itemService.getItemParam(itemId);
	}
}
