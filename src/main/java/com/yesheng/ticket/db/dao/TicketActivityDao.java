package com.yesheng.ticket.db.dao;

import com.yesheng.ticket.db.po.TicketActivity;
import java.util.List;

public interface TicketActivityDao {
  public List<TicketActivity> queryTicketActivityByStatus(int activityStatus);

  public void insertTicketActivity(TicketActivity ticketActivity);

  public TicketActivity queryTicketActivityById(long activityId);

  public void updateTicketActivity(TicketActivity ticketActivity);
}
