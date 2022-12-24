package com.yesheng.ticket.services;

import com.yesheng.ticket.db.dao.TicketActivityDao;
import com.yesheng.ticket.db.po.TicketActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketSaleService {
  @Autowired
  private TicketActivityDao ticketActivityDao;

  public String processSale(long activityId) {
    TicketActivity ticketActivity = ticketActivityDao.queryTicketActivityById(activityId);
    long availableStock = ticketActivity.getAvailableStock();
    String result;

    if (availableStock > 0) {
      result = "Congratulations, your order is complete";
      System.out.println(result);
      availableStock -= 1;
      ticketActivity.setAvailableStock(new Integer("" + availableStock));
      ticketActivityDao.updateTicketActivity(ticketActivity);
    } else {
      result = "Sorry the item is sold out";
      System.out.println(result);
    }
    return result;
  }
}
