package com.taotao.portal.service.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;

/**
 * 
 * @ClassName CartServiceImpl
 * @Description  购物车服务
 * @Author xll
 * @Date 2019年1月8日 下午4:59:56
 * @Version 1.0
 *
 */

@Service
public class CartServiceImpl implements CartService{

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	
	@Value("${ITEM_INFO_URL}")
	private String ITEM_INFO_URL;
	
	/**
	 * 添加购物车商品
	 */
	@Override
	public TaotaoResult addCartItem(HttpServletRequest request , HttpServletResponse response , long itemId, int num) {
	
		CartItem cartItem = null;
		
		//取购物车商品列表
		List<CartItem> list = getCartItemList(request);
		//判断购物车商品列表中是否存在此商品
		for(CartItem temp : list) {
			if(temp.getId() == itemId) {
				//增加商品数量
				temp.setNum(temp.getNum()+num);
				cartItem = temp;
				break;
			}
		}
		if(cartItem == null) {
			cartItem = new CartItem();
			//根据商品id查询商品信息
			String itemData = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId); 
			//把json数据转换成java对象
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(itemData, TbItem.class);
			if(taotaoResult.getStatus() == 200) {
				TbItem tbItem = (TbItem)taotaoResult.getData();
				cartItem.setId(tbItem.getId());
				cartItem.setImage(tbItem.getImage()==null?"":tbItem.getImage().split(",")[0]);
				cartItem.setNum(num);
				cartItem.setPrice(tbItem.getPrice());
				cartItem.setTitle(tbItem.getTitle());
			}
			//添加到购物车列表
			list.add(cartItem);
		}
		//把购物车列表写入cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list),true);

		return TaotaoResult.ok();
	}
	
	/**
	 * 
	 * @Description 从cookie中取商品列表 
	 * @Author xll
	 * @Date 2019年1月8日 下午5:16:11
	 * @return
	 *
	 */
	private List<CartItem> getCartItemList(HttpServletRequest request){
		
		//从cookie中取商品列表
		String cartData = CookieUtils.getCookieValue(request, "TT_CART", true);
		if(cartData == null) {
			return new ArrayList<>();
		}
		try {
			//把json转换成商品列表
			List<CartItem> list = JsonUtils.jsonToList(cartData, CartItem.class);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	/**
	 * 获取商品列表
	 */
	@Override
	public List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) {
		return getCartItemList(request);
	}

	/**
	 * 删除购物车商品
	 */
	@Override
	public TaotaoResult deleteCartItem(HttpServletRequest request, HttpServletResponse response , long itemId) {
		//从cookie中取到购物车商品列表
		List<CartItem> itemList = getCartItemList(request);
		//从列表中找到此商品
		for(CartItem cartItem : itemList) {
			if(cartItem.getId() == itemId) {
				itemList.remove(cartItem);
				break;
			}
		}
		//把购物车列表重新写入cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(itemList),true);
		
		return TaotaoResult.ok();
	}
}
