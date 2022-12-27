package com.yesheng.ticket.mq;

import com.alibaba.fastjson.JSON;
import com.yesheng.ticket.db.dao.TicketActivityDao;
import com.yesheng.ticket.db.po.Order;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RocketMQMessageListener(topic = "payment_complete", consumerGroup = "pay_complete_group")
public class PayCompleteConsumer implements RocketMQListener<MessageExt> {

  @Autowired
  private TicketActivityDao ticketActivityDao;

  @Override
  public void onMessage(MessageExt messageExt) {
    // 1. Generate order
    String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
    log.info("Received order creation request: " + message);
    Order order = JSON.parseObject(message, Order.class);
    // 2. Deduct inventory
    ticketActivityDao.deductStock(order.getTicketActivityId());
  }

}
