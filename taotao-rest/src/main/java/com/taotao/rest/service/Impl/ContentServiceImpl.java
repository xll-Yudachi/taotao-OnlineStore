package com.taotao.rest.service.Impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ContentService;

/**
 * 
 * @ClassName ContentServiceImpl
 * @Description 内容管理 
 * @Author xll
 * @Date 2019年1月2日 下午3:19:26
 * @Version 1.0
 *
 */

@Service
public class ContentServiceImpl implements ContentService{

	@Autowired
	private TbContentMapper tbContentMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${INDEX_CONTENT_REDIS_KEY}")
	private String INDEX_CONTENT_REDIS_KEY;
	
	@Override
	public List<TbContent> getContentList(long contentCid) {
		
		//从缓存中取内容
		try {
			String result = jedisClient.hget(INDEX_CONTENT_REDIS_KEY, contentCid+"");
			if(!StringUtils.isBlank(result)) {
				//把字符串转换成list
				List<TbContent> resultList = JsonUtils.jsonToList(result, TbContent.class);
				//System.out.println(resultList.size());
				return resultList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//根据内容分类id查询内容列表
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(contentCid);
		
		//执行查询
		List<TbContent> list = tbContentMapper.selectByExample(example);
		
		//向redis缓存中添加内容
		try {
			//把list转换为字符串
			String cacheString = JsonUtils.objectToJson(list);
			jedisClient.hset(INDEX_CONTENT_REDIS_KEY,contentCid+"",cacheString);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		return list;
	}
}
