package com.yesheng.ticket.db.dao;

import com.yesheng.ticket.db.po.Ticket;

public interface TicketDao {

  public Ticket queryTicketById(long ticketId);

}
