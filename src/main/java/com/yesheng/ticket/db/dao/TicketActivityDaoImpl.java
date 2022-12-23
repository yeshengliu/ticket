package com.yesheng.ticket.db.dao;

import com.yesheng.ticket.db.mappers.TicketActivityMapper;
import com.yesheng.ticket.db.po.TicketActivity;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class TicketActivityDaoImpl implements TicketActivityDao {

  @Resource
  private TicketActivityMapper ticketActivityMapper;

  @Override
  public List<TicketActivity> queryTicketActivityByStatus(int activityStatus) {
    return ticketActivityMapper.queryTicketActivityByStatus(activityStatus);
  }

  @Override
  public void insertTicketActivity(TicketActivity ticketActivity) {
    ticketActivityMapper.insert(ticketActivity);
  }

  @Override
  public TicketActivity queryTicketActivityById(long activityId) {
    return ticketActivityMapper.selectByPrimaryKey(activityId);
  }

  @Override
  public void updateTicketActivity(TicketActivity ticketActivity) {
    ticketActivityMapper.updateByPrimaryKey(ticketActivity);
  }
}
