package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.DeleteCategoryService;

@Controller
public class DeleteCategoryController {
	
	@Autowired
	private DeleteCategoryService deleteCategoryService;
	
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public TaotaoResult DeleteCategory(Long id) {
		return deleteCategoryService.DeleteCategory(id);
	}
}
