package com.taotao.portal.service.Impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;

/**
 * 
 * @ClassName SearchServiceImpl
 * @Description 商品搜索服务
 * @Author xll
 * @Date 2019年1月4日 下午4:58:27
 * @Version 1.0
 *
 */

@Controller
public class SearchServiceImpl implements SearchService {

	@Value("${SEARCH_BASE_URL}")
	private String SEARCH_BASE_URL;

	@Override
	public SearchResult search(String queryString, Integer page) {
		// 调用taotao-search的服务
		// 查询参数
		Map<String, String> param = new HashMap<>();
		param.put("q", queryString);
		param.put("page", page + "");
		try {
			// 调用服务
			String string = HttpClientUtil.doGet(SEARCH_BASE_URL, param);
			// 把字符串转换为java对象
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(string, SearchResult.class);
			if (taotaoResult.getStatus() == 200) {
				SearchResult searchResult = (SearchResult) taotaoResult.getData();
				return searchResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
