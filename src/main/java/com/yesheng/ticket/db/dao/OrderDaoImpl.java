package com.yesheng.ticket.db.dao;

import com.yesheng.ticket.db.mappers.OrderMapper;
import com.yesheng.ticket.db.po.Order;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl implements OrderDao {

  @Resource
  private OrderMapper orderMapper;

  @Override
  public void insertOrder(Order order) {
    orderMapper.insert(order);
  }

  @Override
  public Order queryOrder(String orderNo) {
    return orderMapper.selectByOrderNo(orderNo);
  }

  @Override
  public void updateOrder(Order order) {
    orderMapper.updateByPrimaryKey(order);
  }
}
