package com.taotao.rest.service.Impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.rest.service.ItemService;

import redis.clients.jedis.JedisCluster;

/**
 * 
 * @ClassName ItemServiceImpl
 * @Description 商品服务 
 * @Author xll
 * @Date 2019年1月4日 下午7:50:18
 * @Version 1.0
 *
 */
@Service
public class ItemServiceImpl implements ItemService{

	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	
	@Autowired
	private TbItemParamItemMapper tbItemParamItemMapper;
	
	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;
	
	@Value("${REDIS_ITEM_EXPIRE}")
	private Integer REDIS_ITEM_EXPIRE;
	
	/**
	 * 根据商品id查询商品信息
	 */
	@Override
	public TaotaoResult getItemBaseInfo(long itemId) {
		//添加缓存逻辑
		//从缓存中取商品信息,商品id对应的信息
		try {
			String result = jedisCluster.get(REDIS_ITEM_KEY + ":" + itemId + ":base");
			//判断result是否有值
			if(!StringUtils.isBlank(result)) {
				//把result转换成java对象
				TbItem tbItem = JsonUtils.jsonToPojo(result, TbItem.class);
				return TaotaoResult.ok(tbItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
		
		try {
			//把商品信息写入缓存中
			jedisCluster.set(REDIS_ITEM_KEY + ":" + itemId + ":base", JsonUtils.objectToJson(item));
			//设置key的有效期
			jedisCluster.expire(REDIS_ITEM_KEY + ":" + itemId + ":base",REDIS_ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return TaotaoResult.ok(item);
	}

	/**
	 * 查询商品描述
	 */
	@Override
	public TaotaoResult getItemDesc(long itemId) {
		//添加缓存逻辑
		try {
			//从商品中取商品信息
			String result = jedisCluster.get(REDIS_ITEM_KEY + ":" + itemId + ":desc");
			//判断是否有值
			if(!StringUtils.isBlank(result)) {
				//把json转换成java对象
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(result, TbItemDesc.class);
				return TaotaoResult.ok(itemDesc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//创建查询条件
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		
		try {
			//把商品信息写入缓存
			jedisCluster.set(REDIS_ITEM_KEY + ":" + itemId + ":desc" , JsonUtils.objectToJson(itemDesc));
			//设置缓存过期时间
			jedisCluster.expire(REDIS_ITEM_KEY + ":" + itemId + ":desc" , REDIS_ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return TaotaoResult.ok(itemDesc);
	}

	/**
	 * 查询商品模板
	 */
	@Override
	public TaotaoResult getItemParam(long itemId) {
		//添加缓存逻辑
		//从缓存中取出商品信息
		String result = jedisCluster.get(REDIS_ITEM_KEY + ":" + itemId + ":param");
		//判断是否有值
		if(!StringUtils.isBlank(result)) {
			//把json转换成java对象
			TbItemParamItem paramItem = JsonUtils.jsonToPojo(result, TbItemParamItem.class);
			return TaotaoResult.ok(paramItem);
		}
		
		//根据商品id查询规格参数
		//设置查询条件
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		//执行查询
		List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
		
		
		//添加到缓存中
		try {
			//添加缓存信息
			jedisCluster.set(REDIS_ITEM_KEY + ":" + itemId + ":param", JsonUtils.objectToJson(list));
			//添加缓存过期时间
			jedisCluster.expire(REDIS_ITEM_KEY + ":" + itemId + ":param", REDIS_ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if(list!=null && list.size()>0) {
			TbItemParamItem paramItem = list.get(0);
			return TaotaoResult.ok(paramItem);
		}
		return TaotaoResult.build(400, "无此商品规格");
	}
}
