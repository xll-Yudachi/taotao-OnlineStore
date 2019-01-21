package com.taotao.service.Impl;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.reflection.wrapper.BaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;


@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	
	@Autowired
	private TbItemParamMapper tbItemParamMapper;
	
	@Autowired
	private TbItemParamItemMapper tbItemParamItemMapper;
	
	@Override
	public TbItem getItemById(long itemId) {
		
		//TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//添加查询条件
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			TbItem item = list.get(0);
			return item;
		}
		return null;
	}

	@Override
	public EUDataGridResult getItemList(int page, int rows) {
		//查询商品列表
		TbItemExample example = new TbItemExample();
		//分页处理
		PageHelper.startPage(page, rows);
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(list);
		//取记录总条数
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	@Override
	public EUDataGridResult getParamList(int page,int rows) {
		//查询参数规格列表
		TbItemParamExample example = new TbItemParamExample();
		//分页处理
		PageHelper.startPage(page, rows);
		List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
		//创建一个返回值对象
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(list);
		//取记录总条数
		PageInfo<TbItemParam> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}
	//查询商品种类
	@Override
	public List<TbItemCat> getItemCatList(long parentId) {
		
		TbItemCatExample example = new TbItemCatExample();
		//设置查询条件
		com.taotao.pojo.TbItemCatExample.Criteria criteria = example.createCriteria();
		//根据parentId查询子节点
		criteria.andParentIdEqualTo(parentId);
		//返回子节点列表
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		
		return list;
	}

	@Override
	public TaotaoResult createItem(TbItem item,String desc,String itemParam) throws Exception{
		//item补全
		//生成商品ID
		Long itemId = IDUtils.genItemId();
		item.setId(itemId);
		//生成商品状态
		item.setStatus((byte)1);
		//生成创建日期
		item.setCreated(new Date());
		//生成更新日期
		item.setUpdated(new Date());
		//插入数据库
		itemMapper.insert(item);
		//添加商品描述信息
		TaotaoResult result = insertItemDesc(itemId, desc);
		if(result.getStatus()!=200) {
			throw new Exception();
		}
		//添加规格参数
		result = insertItemParamItem(itemId, itemParam);
		if(result.getStatus()!=200) {
			throw new Exception();
		}
		return TaotaoResult.ok();
	}
	
	private TaotaoResult insertItemDesc(Long itemId,String desc) {
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		tbItemDescMapper.insert(itemDesc);
		return TaotaoResult.ok();
	}
	
	private TaotaoResult insertItemParamItem(Long itemId,String itemParam) {
		TbItemParamItem tbItemParamItem = new TbItemParamItem();
		tbItemParamItem.setItemId(itemId);
		tbItemParamItem.setParamData(itemParam);
		tbItemParamItem.setCreated(new Date());
		tbItemParamItem.setUpdated(new Date());
		tbItemParamItemMapper.insert(tbItemParamItem);
		return TaotaoResult.ok();
	}
}