package com.yesheng.ticket.db.dao;

import com.yesheng.ticket.db.mappers.TicketActivityMapper;
import com.yesheng.ticket.db.po.TicketActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
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

  @Override
  public boolean lockStock(Long ticketActivityId) {
    int result = ticketActivityMapper.lockStock(ticketActivityId);
    if (result < 1) {
      log.error("Failed to lock inventory");
      return false;
    }
    return true;
  }

  @Override
  public boolean deductStock(Long ticketActivityId) {
    int result = ticketActivityMapper.deductStock(ticketActivityId);
    if (result < 1) {
      log.error("Failed to deduct inventory");
      return false;
    }
    return true;
  }
}
