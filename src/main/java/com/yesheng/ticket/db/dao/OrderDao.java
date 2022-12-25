package com.yesheng.ticket.db.dao;

import com.yesheng.ticket.db.po.Order;

public interface OrderDao {

  void insertOrder(Order order);

  Order queryOrder(String orderNo);

  void updateOrder(Order order);
}
