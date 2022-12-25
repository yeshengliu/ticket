package com.yesheng.ticket.component;

import com.yesheng.ticket.db.dao.TicketActivityDao;
import com.yesheng.ticket.db.po.TicketActivity;
import com.yesheng.ticket.util.RedisService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RedisPreheatRunner implements ApplicationRunner {
  @Autowired
  RedisService redisService;

  @Autowired
  TicketActivityDao ticketActivityDao;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    List<TicketActivity> ticketActivities = ticketActivityDao.queryTicketActivityByStatus(1);
    for (TicketActivity ticketActivity : ticketActivities) {
      redisService.setValue("stock:" + ticketActivity.getId(), (long) ticketActivity.getAvailableStock());
    }
  }
}
