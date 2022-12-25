package com.yesheng.ticket.services;

import com.yesheng.ticket.util.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketActivityService {
  @Autowired
  private RedisService redisService;

  public boolean ticketStockValidator(long activityId) {
    String key = "stock:" + activityId;
    return redisService.stockDeductValidator(key);
  }
}
