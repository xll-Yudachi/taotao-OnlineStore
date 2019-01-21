package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;
import com.taotao.service.ItemParamService;

/*
 * 规格参数的模板管理
 */
@Controller
public class ItemParamController {
	
	@Autowired
	private ItemParamService itemParamService;
	
	@RequestMapping("/item/param/query/itemcatid/{itemCatId}")
	@ResponseBody
	public TaotaoResult getItemParamByCid(@PathVariable Long itemCatId) {
		return itemParamService.getItemParamByCid(itemCatId);
	}
	
	@RequestMapping("/item/param/save/{cid}")
	@ResponseBody
	public TaotaoResult insertItemParam(@PathVariable Long cid,String paramData) {
		//创建Pojo对象
		TbItemParam tbItemParam = new TbItemParam();
		tbItemParam.setItemCatId(cid);
		tbItemParam.setParamData(paramData);
		TaotaoResult result = itemParamService.insertItemParam(tbItemParam);
		return result;
	}
}
