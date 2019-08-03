package com.chen.miaosha.rabbitmq;

import com.chen.miaosha.controller.MiaoshaController;
import com.chen.miaosha.redis.GoodsKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chen.miaosha.domain.MiaoshaOrder;
import com.chen.miaosha.domain.MiaoshaUser;
import com.chen.miaosha.redis.RedisService;
import com.chen.miaosha.service.GoodsService;
import com.chen.miaosha.service.MiaoshaService;
import com.chen.miaosha.service.OrderService;
import com.chen.miaosha.vo.GoodsVo;

@Service
public class MQReceiver {

		private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
		
		@Autowired
		RedisService redisService;
		
		@Autowired
		GoodsService goodsService;
		
		@Autowired
		OrderService orderService;
		
		@Autowired
		MiaoshaService miaoshaService;
		
		@RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
		public void receive(String message) {
			log.info("receive message:"+message);
			MiaoshaMessage mm  = RedisService.stringToBean(message, MiaoshaMessage.class);
			MiaoshaUser user = mm.getUser();
			long goodsId = mm.getGoodsId();
			
			GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
	    	int stock = goods.getStockCount();
	    	if(stock <= 0) {
	    		return;
	    	}
	    	//判断是否已经秒杀到了
	    	MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
	    	if(order != null) {
				redisService.incr(GoodsKey.getMiaoshaGoodsStock, ""+goodsId);//+1  //前面还可以继续秒杀
				MiaoshaController.localOverMap.put(goodsId, false);//放开
	    		return;
	    	}
	    	//减库存 下订单 写入秒杀订单
	    	miaoshaService.miaosha(user, goods);
		}
}
