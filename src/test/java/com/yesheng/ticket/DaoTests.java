package com.yesheng.ticket;

import com.yesheng.ticket.db.dao.OrderDao;
import com.yesheng.ticket.db.dao.TicketActivityDao;
import com.yesheng.ticket.db.mappers.TicketActivityMapper;
import com.yesheng.ticket.db.po.Order;
import com.yesheng.ticket.db.po.TicketActivity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DaoTests {

  @Resource
  private TicketActivityMapper ticketActivityMapper;

  @Autowired
  private TicketActivityDao ticketActivityDao;

  @Autowired
  private OrderDao orderDao;

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

  @Test
  void TicketOrderTest() {
    Order order = new Order();
    order.setOrderNo("524744128538480650");
    order.setOrderStatus(1);
    order.setTicketActivityId(19L);
    order.setUserId(1234L);
    order.setOrderAmount(750L);
    order.setCreateTime(new Date());
    order.setPayTime(new Date());
    orderDao.insertOrder(order);
    System.out.println("====>>>>" +
        orderDao.queryOrder("524744128538480650"));
  }
}
