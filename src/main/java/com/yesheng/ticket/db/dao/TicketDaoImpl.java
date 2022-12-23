package com.yesheng.ticket.db.dao;

import com.yesheng.ticket.db.mappers.TicketMapper;
import com.yesheng.ticket.db.po.Ticket;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class TicketDaoImpl implements TicketDao {

  @Resource
  private TicketMapper ticketMapper;

  @Override
  public Ticket queryTicketById(long ticketId) {
    return ticketMapper.selectByPrimaryKey(ticketId);
  }
}
