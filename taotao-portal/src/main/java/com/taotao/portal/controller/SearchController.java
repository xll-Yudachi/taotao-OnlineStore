package com.taotao.portal.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;

/**
 * 
 * @ClassName SearchController
 * @Description 商品查询controller
 * @Author xll
 * @Date 2019年1月4日 下午5:08:56
 * @Version 1.0
 *
 */
@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;

	@RequestMapping("/search")
	public String search(@RequestParam("q") String queryString, @RequestParam(defaultValue="1")Integer page, Model model) {

		try {
			if (!StringUtils.isBlank(queryString)) {
				queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SearchResult searchResult = searchService.search(queryString, page);
		// 向页面传递参数
		model.addAttribute("query", queryString);
		model.addAttribute("totalPages", searchResult.getPageCount());
		model.addAttribute("itemList", searchResult.getItemList());
		model.addAttribute("page", page);

		return "search";
	}
}
