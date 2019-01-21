package com.taotao.portal.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.pojo.CartItem;

public interface CartService {

	TaotaoResult addCartItem(HttpServletRequest request ,HttpServletResponse response , long itemId, int num);
	List<CartItem> getCartItemList(HttpServletRequest request , HttpServletResponse response);
	TaotaoResult deleteCartItem(HttpServletRequest request, HttpServletResponse response ,long itemId);
	
}
