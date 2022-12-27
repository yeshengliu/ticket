package com.yesheng.ticket.mq;

import com.alibaba.fastjson.JSON;
import com.yesheng.ticket.db.dao.OrderDao;
import com.yesheng.ticket.db.dao.TicketActivityDao;
import com.yesheng.ticket.db.po.Order;
import com.yesheng.ticket.util.RedisService;
import java.nio.charset.StandardCharsets;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RocketMQMessageListener(topic = "payment_check", consumerGroup = "payment_check_group")
public class PayStatusCheckListener implements RocketMQListener<MessageExt> {

  @Autowired
  private OrderDao orderDao;

  @Autowired
  private TicketActivityDao ticketActivityDao;

  @Resource
  private RedisService redisService;

  @Override
  @Transactional
  public void onMessage(MessageExt messageExt) {
    String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
    log.info("Received order payment verification message: " + message);
    Order order = JSON.parseObject(message, Order.class);
    // 1. Lookup the order
    Order orderInfo = orderDao.queryOrder(order.getOrderNo());
    // 2. Check the payment information
    if (orderInfo.getOrderStatus() != 2) {
      // 3. Delete the order with no payment
      log.info("Delete the order due to payment incomplete, order no: " + orderInfo.getOrderNo());
      orderInfo.setOrderStatus(99);
      orderDao.updateOrder(orderInfo);
      // 4. Recover inventory
      ticketActivityDao.revertStock(order.getTicketActivityId());
      // 5. Recover redis inventory
      redisService.revertStock("stock:" + order.getTicketActivityId());
    }
  }
}
