 package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemCat;

public interface ItemService {

	TbItem getItemById(long itemId);
	EUDataGridResult getItemList(int page, int rows);
	EUDataGridResult getParamList(int page,int rows);
	List<TbItemCat> getItemCatList(long parentId);
	TaotaoResult createItem(TbItem item,String desc,String itemParam) throws Exception;
}
