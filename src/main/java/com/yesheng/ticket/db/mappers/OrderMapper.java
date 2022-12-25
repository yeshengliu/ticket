package com.yesheng.ticket.db.mappers;

import com.yesheng.ticket.db.po.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

  Order selectByOrderNo(String orderNo);
}