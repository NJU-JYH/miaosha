package com.imooc.miaosha.service;

import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.vo.GoodsVo;

@Service
public class MiaoshaService {
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;

	@Autowired
	RedisService redisService;

	@Transactional
	public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
		//减库存 下订单 写入秒杀订单
		boolean flag= goodsService.reduceStock(goods);
		//order_info maiosha_order
		OrderInfo orderInfo;
		if(flag) orderInfo=orderService.createOrder(user, goods);
		else {
			orderInfo=null;
			redisService.incr(GoodsKey.getMiaoshaGoodsStock,""+goods.getId());
		}

		return orderInfo;
	}
	
}
