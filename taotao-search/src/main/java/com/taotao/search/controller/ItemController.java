package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.ItemService;

/**
 * 
 * @ClassName ItemController
 * @Description 索引库维护 
 * @Author xll
 * @Date 2019年1月4日 下午3:04:40
 * @Version 1.0
 *
 */
@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 
	 * @Description 导入商品数据到索引库 
	 * @Author xll
	 * @Date 2019年1月4日 下午3:05:44
	 * @return
	 *
	 */
	@RequestMapping("/manager/importAll")
	@ResponseBody
	public TaotaoResult importAllItems() {
		return itemService.importAllItems();
	}
}
