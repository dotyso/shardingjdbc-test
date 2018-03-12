package com.divino.shardingjdbc.service;

import com.divino.shardingjdbc.entity.Order;
import com.divino.shardingjdbc.entity.OrderItem;
import com.divino.shardingjdbc.repository.OrderItemRepository;
import com.divino.shardingjdbc.repository.OrderRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DemoService {
    
    @Resource
    private OrderRepository orderRepository;
    
    @Resource
    private OrderItemRepository orderItemRepository;
    
    public void demo() {
        //orderRepository.createIfNotExistsTable();
        //orderItemRepository.createIfNotExistsTable();
        //orderRepository.truncateTable();
        //orderItemRepository.truncateTable();
        
        List<Long> orderIds = new ArrayList<>(10);
        System.out.println("1.Insert--------------");
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setUserId(51);
            order.setStatus("INSERT_TEST");
            orderRepository.insert(order);
            long orderId = order.getOrderId();
            orderIds.add(orderId);
            
            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setUserId(51);
            //item.setStatus("INSERT_TEST");
            item.setCreateDate(new Date());
            orderItemRepository.insert(item);
        }
        System.out.println(orderItemRepository.selectAll());
        
        /*
        System.out.println("2.Delete--------------");
        for (Long each : orderIds) {
            orderRepository.delete(each);
            orderItemRepository.delete(each);
        }
        System.out.println(orderItemRepository.selectAll());
        */
        //orderItemRepository.dropTable();
        //orderRepository.dropTable();
    }
}
