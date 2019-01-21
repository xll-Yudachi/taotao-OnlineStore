package com.taotao.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.service.DeleteCategoryService;

/**
 * 
 * @ClassName DeleteCategoryServiceImpl
 * @Description 删除商品分类服务
 * @Author xll
 * @Date 2018年12月28日 下午12:09:42
 * @Version 1.0
 *
 */
@Service
public class DeleteCategoryServiceImpl implements DeleteCategoryService{

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	@Override
	public TaotaoResult DeleteCategory(long id) {
		deleteCategoryAndChildNode(id);
		return TaotaoResult.ok();
	}
	
	/**
	 * 
	 * @Description 获得所有该节点下的孩子节点 
	 * @Author xll
	 * @Date 2018年12月28日 下午12:57:07
	 * @param id 父节点Id
	 * @return List<TbContentCategory>
	 *
	 */
	private List<TbContentCategory> getChildNodeList(Long id){
		 //查询所有父节点为传入id的结点
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		//返回所有符合要求的节点
		return tbContentCategoryMapper.selectByExample(example);
	}
	
	private void deleteCategoryAndChildNode(Long id) {
		//获取要删除的Categoty
		TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);
		//判断是否为父节点
		if(category.getIsParent()) {
			//是父节点,获得该节点下的孩子节点
			List<TbContentCategory> list = getChildNodeList(id);
			//删除所有的孩子节点
			for(TbContentCategory temp : list) {
				deleteCategoryAndChildNode(temp.getId());
			}
		}
		//不是父节点 判断父节点下是否还有其他子节点
		if(getChildNodeList(category.getParentId()).size()==1) {
			//没有则将父节点标记为叶子节点
			TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(category.getParentId());
			parent.setIsParent(false);
			tbContentCategoryMapper.updateByPrimaryKey(parent);
		}
		//删除本节点
		tbContentCategoryMapper.deleteByPrimaryKey(id);
	}
}
