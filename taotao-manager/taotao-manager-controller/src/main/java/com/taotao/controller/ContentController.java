package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;

/**
 * 
 * @ClassName ContentController
 * @Description 内容管理Controller
 * @Author xll
 * @Date 2019年1月2日 下午1:44:49
 * @Version 1.0
 *
 */
@Controller
public class ContentController {

	@Autowired
	ContentService contentService;
	
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EUDataGridResult queryContent(Integer page,Integer rows,Long categoryId) {
		return contentService.queryContent(page, rows, categoryId);
	}
	
	@RequestMapping("/content/save")
	@ResponseBody
	public TaotaoResult insertContent(TbContent content) {
		return contentService.insertContent(content);
	}
}
