package com.yesheng.ticket;

import com.yesheng.ticket.db.dao.TicketActivityDao;
import com.yesheng.ticket.db.mappers.TicketActivityMapper;
import com.yesheng.ticket.db.po.TicketActivity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class DaoTests {

  @Resource
  private TicketActivityMapper ticketActivityMapper;

  @Autowired
  private TicketActivityDao ticketActivityDao;

  @Test
  void TicketActivityTest() {
    TicketActivity ticketActivity = new TicketActivity();
    ticketActivity.setName("Test");
    ticketActivity.setTicketId(999L);
    ticketActivity.setTotalStock(100L);
    ticketActivity.setSalePrice(new BigDecimal(99));
    ticketActivity.setActivityStatus(16);
    ticketActivity.setOriginalPrice(new BigDecimal(99));
    ticketActivity.setAvailableStock(100);
    ticketActivity.setLockStock(0L);
    ticketActivityMapper.insert(ticketActivity);
    System.out.println("====>>>>" +
        ticketActivityMapper.selectByPrimaryKey(1L));
  }

  @Test
  void setTicketActivityQuery(){
    List<TicketActivity> ticketActivities =
        ticketActivityDao.queryTicketActivityByStatus(0);
    System.out.println(ticketActivities.size());
    ticketActivities.stream().forEach(ticketActivity ->
        System.out.println(ticketActivity.toString()));
  }

}
