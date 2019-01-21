package com.taotao.order.service.Impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

import redis.clients.jedis.JedisCluster;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private TbOrderMapper tbOrderMapper;
	
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	
	@Autowired
	private TbOrderShippingMapper tbOrderShippingMapper;
	
	@Autowired
	private JedisCluster JedisCluster;
	
	@Value("${ORDER_GEN_KEY}")
	private String ORDER_GEN_KEY;
	
	@Value("${ORDER_INIT_ID}")
	private String ORDER_INIT_ID;
	
	@Value("${ORDER_DETAIL_GEN_KEY}")
	private String ORDER_DETAIL_GEN_KEY;
	
	/**
	 * 创建订单
	 */
	@Override
	public TaotaoResult createOrder(TbOrder order, List<TbOrderItem> itemList, TbOrderShipping orderShipping) {
		//向订单表中插入记录
		
		//获得订单号
		String Order_Gen_Key = JedisCluster.get(ORDER_GEN_KEY);
		if(StringUtils.isBlank(Order_Gen_Key)) {
			JedisCluster.set(ORDER_GEN_KEY, ORDER_INIT_ID);
		}
		long Order_Id = JedisCluster.incr(ORDER_GEN_KEY);
		//补全pojo属性
		order.setOrderId(Order_Id + "");
		order.setStatus(1);	//未付款
		order.setCreateTime(new Date());
		order.setUpdateTime(new Date());
		order.setBuyerRate(0); //买家未评价
		//向订单表中插入数据
		tbOrderMapper.insert(order);
		//插入订单明细
		for(TbOrderItem orderItem : itemList) {
			//补全订单明细
			//取订单明细id
			long Order_Detail_Id = JedisCluster.incr(ORDER_DETAIL_GEN_KEY);
			orderItem.setId(Order_Detail_Id + "");
			orderItem.setOrderId(Order_Id + "");
			//向订单明细插入记录
			tbOrderItemMapper.insert(orderItem);
		}
		//插入物流表
		//补全物流表pojo
		orderShipping.setOrderId(Order_Id + "");
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		tbOrderShippingMapper.insert(orderShipping);
		
		return TaotaoResult.ok(Order_Id);
	}
}
