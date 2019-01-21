package com.taotao.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentCategoryService;

/**
 * 
 * @ClassName ContentCategoryServiceImpl
 * @Description 内容分类管理服务
 * @Author xll
 * @Date 2018年12月28日 上午9:29:01
 * @Version 1.0
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	@Override
	public List<EUTreeNode> getCategoryList(long parentId) {
		//根据parentId查询节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		List<EUTreeNode> resultList = new ArrayList<>();
		//添加节点
		for(TbContentCategory temp : list) {
			//创建一个节点
			EUTreeNode node = new EUTreeNode();
			node.setId(temp.getId());
			node.setText(temp.getName());
			node.setState(temp.getIsParent()?"closed":"open");
			
			resultList.add(node);
		}
		return resultList;
	}

	@Override
	public TaotaoResult insertContentCategory(long parentId, String name) {
		//创建一个pojo
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setName(name);
		tbContentCategory.setIsParent(false);
		tbContentCategory.setStatus(1);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		//添加记录
		tbContentCategoryMapper.insert(tbContentCategory);
		//查看父节点的isParent是否为true,如果不是true就改成true
		TbContentCategory parentCat = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parentCat.getIsParent()) {
			parentCat.setIsParent(true);
			//更新父节点
			tbContentCategoryMapper.updateByPrimaryKey(parentCat);
		}
		//返回结果
		return TaotaoResult.ok(tbContentCategory);
	}

	@Override
	public TaotaoResult updateContentCategory(long id, String name) {
		
		TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);
		category.setName(name);
		tbContentCategoryMapper.updateByPrimaryKey(category);
		
		return TaotaoResult.ok();
	}
}
