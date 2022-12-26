package com.yesheng.ticket.services;

import com.alibaba.fastjson.JSON;
import com.yesheng.ticket.db.dao.TicketActivityDao;
import com.yesheng.ticket.db.po.Order;
import com.yesheng.ticket.db.po.TicketActivity;
import com.yesheng.ticket.mq.RocketMQService;
import com.yesheng.ticket.util.RedisService;
import com.yesheng.ticket.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketActivityService {
  @Autowired
  private RedisService redisService;

  @Autowired
  private TicketActivityDao ticketActivityDao;

  @Autowired
  private RocketMQService rocketMQService;

  private SnowFlake snowFlake = new SnowFlake(1, 1);

  public Order createOrder(long ticketActivityId, long userId) throws Exception {
    /*
     * 1. Generate order
     */
    TicketActivity ticketActivity = ticketActivityDao.queryTicketActivityById(ticketActivityId);
    Order order = new Order();
    // use snowflake id generator
    order.setOrderNo(String.valueOf(snowFlake.nextId()));
    order.setTicketActivityId(ticketActivity.getId());
    order.setUserId(userId);
    order.setOrderAmount(ticketActivity.getSalePrice().longValue());

    /*
     * 2. Send order information
     */
    rocketMQService.sendMessage("ticket_order", JSON.toJSONString(order));

    return order;
  }

  public boolean ticketStockValidator(long activityId) {
    String key = "stock:" + activityId;
    return redisService.stockDeductValidator(key);
  }
}
