package com.taotao.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.rest.service.RedisService;

/**
 * 
 * @ClassName RedisController
 * @Description 缓存同步Controller 
 * @Author xll
 * @Date 2019年1月3日 下午4:53:24
 * @Version 1.0
 *
 */

@Controller
public class RedisController {
	
	@Autowired
	private RedisService redisService;
	
	@RequestMapping("/cache/sync/content/{contentCid}")
	@ResponseBody
	public TaotaoResult contentCacheSync(@PathVariable long contentCid) {
		return redisService.syncContent(contentCid);
	}
}
