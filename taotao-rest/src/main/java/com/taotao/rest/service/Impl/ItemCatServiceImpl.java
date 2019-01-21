package com.taotao.rest.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;

/**
 * 
 * @ClassName ItemCatServiceImpl
 * @Description 商品分类服务
 * @Author xll
 * @Date 2018年12月27日 下午6:36:38
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("%{INDEX_CATEGORY_REDIS_KEY}")
	private String INDEX_CATEGORY_REDIS_KEY;
	
	@Override
	public CatResult getItemCatList() {
		 try {
			String result = jedisClient.get(INDEX_CATEGORY_REDIS_KEY);
			if(!StringUtils.isBlank(result)) {
				List<CatNode> resultList = JsonUtils.jsonToList(result, CatNode.class);
				CatResult catResult = new CatResult();
				catResult.setData(resultList);
				return catResult;
			}
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 
		 CatResult catResult = new CatResult();
		 //查询分类列表
		 catResult.setData(getCatList(0));
		 return catResult;
	}
	
	/**
	 * 
	 * @Description 查询分类列表
	 * @Author xll
	 * @Date 2018年12月27日 下午6:52:10
	 * @param parentId
	 * @return
	 *
	 */
	private List<?> getCatList(long parentId){		
		//创建查询条件
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
		//返回值list
		List resultList = new ArrayList<>();
		//向resultList中添加节点
		int count=0;
		for(TbItemCat temp : list) {
			//判断是否为父节点
			if(temp.getIsParent()) {
				CatNode catNode = new CatNode();
				if(parentId==0) {
					catNode.setName("<a href='/products/"+temp.getId()+".html'>"+temp.getName()+"</a>");
				}else {
					catNode.setName(temp.getName());
				}
				catNode.setUrl("/products/"+temp.getId()+".html");
				catNode.setItem(getCatList(temp.getId()));
				
				resultList.add(catNode);
				count++;
				//第一层只取14条记录
				if(parentId == 0 && count >= 14) {
					break;
				}
			//如果是叶子节点
			}else {
				resultList.add("/products/"+temp.getId()+".html|" + temp.getName());
			}
		}
		
		//向redis中添加缓存
		try {
			String redisString = JsonUtils.objectToJson(resultList);
			jedisClient.set(INDEX_CATEGORY_REDIS_KEY, redisString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

}
