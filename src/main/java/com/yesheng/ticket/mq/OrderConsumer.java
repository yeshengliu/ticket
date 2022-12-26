package com.yesheng.ticket.mq;

import com.alibaba.fastjson.JSON;
import com.yesheng.ticket.db.dao.OrderDao;
import com.yesheng.ticket.db.dao.TicketActivityDao;
import com.yesheng.ticket.db.po.Order;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RocketMQMessageListener(topic = "ticket_order", consumerGroup = "ticket_order_group")
public class OrderConsumer implements RocketMQListener<MessageExt> {

  @Autowired
  private OrderDao orderDao;

  @Autowired
  private TicketActivityDao ticketActivityDao;

  @Override
  @Transactional
  public void onMessage(MessageExt messageExt) {
    // 1. Parse order information
    String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
    log.info("Received create order request: " + message);
    Order order = JSON.parseObject(message, Order.class);
    order.setCreateTime(new Date());
    // 2. Deduct inventory
    boolean lockStockResult = ticketActivityDao.lockStock(order.getTicketActivityId());
    if (lockStockResult) {
      // Order Status 0: No available inventory, invalid order; 1: Order created, payment pending
      order.setOrderStatus(1);
    } else {
      order.setOrderStatus(0);
    }
    // 3. Insert order
    orderDao.insertOrder(order);
  }
}
