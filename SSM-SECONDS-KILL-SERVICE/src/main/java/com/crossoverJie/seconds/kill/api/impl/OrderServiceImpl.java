package com.crossoverJie.seconds.kill.api.impl;

import org.springframework.stereotype.Service;
import com.crossoverJie.seconds.kill.service.OrderService;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import com.crossoverJie.seconds.kill.api.OrderServiceFacade;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 01/05/2018 14:01
 * @since JDK 1.8
 */
@Service("orderServiceFacade")
public class OrderServiceImpl implements OrderServiceFacade {

	/**
    @Resource(name = "DBOrderService")
    private com.crossoverJie.seconds.kill.service.OrderService orderService ;  **/
    
    @Autowired
    private OrderService orderService;

    @Override
    public int createWrongOrder(int sid) throws Exception {
        return orderService.createWrongOrder(sid);
    }

    @Override
    public int createOptimisticOrder(int sid) throws Exception {
        return orderService.createOptimisticOrder(sid);
    }

    @Override
    public int createOptimisticOrderUseRedis(int sid) throws Exception {
        return orderService.createOptimisticOrderUseRedis(sid);
    }

    @Override
    public void createOptimisticOrderUseRedisAndKafka(int sid) throws Exception {
        orderService.createOptimisticOrderUseRedisAndKafka(sid);
    }
}
