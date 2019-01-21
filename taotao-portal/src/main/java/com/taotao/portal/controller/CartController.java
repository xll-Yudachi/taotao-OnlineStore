package com.taotao.portal.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;

/**
 * 
 * @ClassName CartController
 * @Description 购物车controller 
 * @Author xll
 * @Date 2019年1月8日 下午5:34:36
 * @Version 1.0
 *
 */
@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@RequestMapping("/add/{itemId}")
	public String addCartItem(
							  @PathVariable Long itemId,
							  @RequestParam(defaultValue="1") Integer num,
							  HttpServletRequest request,
							  HttpServletResponse response
							 ) {
		
		TaotaoResult taotaoResult = cartService.addCartItem(request, response, itemId, num);
		return "redirect:/cart/success.html";
	}
	
	@RequestMapping("/success")
	public String showSuccess() {
		return "cartSuccess";
	}
	
	@RequestMapping("/cart")
	public String showCart(HttpServletRequest request,HttpServletResponse response,Model model) {
		List<CartItem> list = cartService.getCartItemList(request, response);
		model.addAttribute("cartList", list);
		return "cart";
	}
	
	@RequestMapping("/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response) {
		cartService.deleteCartItem(request, response, itemId);
		return "redirect:/cart/cart.html";
	}
}
