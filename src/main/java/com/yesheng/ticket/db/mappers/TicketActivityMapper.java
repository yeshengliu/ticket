package com.yesheng.ticket.db.mappers;

import com.yesheng.ticket.db.po.TicketActivity;
import java.util.List;

public interface TicketActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TicketActivity record);

    int insertSelective(TicketActivity record);

    TicketActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TicketActivity record);

    int updateByPrimaryKey(TicketActivity record);

    List<TicketActivity> queryTicketActivityByStatus(int activityStatus);

    int lockStock(Long id);

    int deductStock(Long id);

    void revertStock(Long id);
}