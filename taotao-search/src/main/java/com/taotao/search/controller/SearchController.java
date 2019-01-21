package com.taotao.search.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.pojo.SearchResult;
import com.taotao.search.service.SearchService;

/**
 * 
 * @ClassName SearchController
 * @Description 商品查询 
 * @Author xll
 * @Date 2019年1月4日 下午3:43:01
 * @Version 1.0
 *
 */

@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@RequestMapping(value = "/query", method=RequestMethod.GET)
	@ResponseBody
	public TaotaoResult search(
							   @RequestParam("q") String queryString,
							   @RequestParam(defaultValue="1")Integer page,
							   @RequestParam(defaultValue="60")Integer rows
							  ) {
		
		SearchResult searchResult = null;
		
		if(StringUtils.isBlank(queryString)) {
			return TaotaoResult.build(400, "查询条件不能为空");
		}
		try {
			 queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
			 searchResult = searchService.search(queryString, page, rows);
		} catch (Exception e) {
			e.printStackTrace();
			TaotaoResult.build(500, e.getMessage());
		}
		return TaotaoResult.ok(searchResult);
	}
}
