package com.yesheng.ticket.web;

import com.yesheng.ticket.db.dao.TicketActivityDao;
import com.yesheng.ticket.db.dao.TicketDao;
import com.yesheng.ticket.db.po.Ticket;
import com.yesheng.ticket.db.po.TicketActivity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TicketActivityController {

  @Autowired
  private TicketActivityDao ticketActivityDao;

  @Autowired
  private TicketDao ticketDao;

  @RequestMapping("/item/{ticketActivityId}")
  public String itemPage(Map<String, Object> resultMap, @PathVariable long ticketActivityId) {
    TicketActivity ticketActivity = ticketActivityDao.queryTicketActivityById(ticketActivityId);
    Ticket ticket = ticketDao.queryTicketById(ticketActivity.getTicketId());

    resultMap.put("ticketActivity", ticketActivity);
    resultMap.put("ticket", ticket);
    resultMap.put("salePrice", ticketActivity.getSalePrice());
    resultMap.put("originalPrice", ticketActivity.getOriginalPrice());
    resultMap.put("ticketId", ticketActivity.getTicketId());
    resultMap.put("ticketName", ticket.getTicketName());
    resultMap.put("ticketDesc", ticket.getTicketDesc());
    return "ticket_item";
  }

  @RequestMapping("/activities")
  public String activityList(Map<String, Object> resultMap) {
    List<TicketActivity> ticketActivities = ticketActivityDao.queryTicketActivityByStatus(1);
    resultMap.put("ticketActivities", ticketActivities);
    return "ticket_activity";
  }

  @RequestMapping("/addTicketActivity")
  public String addTicketActivity() {
    return "add_activity";
  }

  @ResponseBody
  @RequestMapping("/addTicketActivityAction")
  public String addTicketActivityAction(
      @RequestParam("name") String name,
      @RequestParam("ticketId") long ticketId,
      @RequestParam("salePrice") BigDecimal salePrice,
      @RequestParam("originalPrice") BigDecimal originalPrice,
      @RequestParam("totalStock") long totalStock,
      @RequestParam("startTime") String startTime,
      @RequestParam("endTime") String endTime,
      Map<String, Object> resultMap
  ) throws ParseException {
    startTime = startTime.substring(0, 10) + startTime.substring(11);
    endTime = endTime.substring(0, 10) + endTime.substring(11);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddhh:mm");
    TicketActivity ticketActivity = new TicketActivity();
    ticketActivity.setName(name);
    ticketActivity.setTicketId(ticketId);
    ticketActivity.setSalePrice(salePrice);
    ticketActivity.setOriginalPrice(originalPrice);
    ticketActivity.setTotalStock(totalStock);
    ticketActivity.setAvailableStock(new Integer("" + totalStock));
    ticketActivity.setLockStock(0L);
    ticketActivity.setActivityStatus(1);
    ticketActivity.setStartTime(format.parse(startTime));
    ticketActivity.setEndTime(format.parse(endTime));
    ticketActivityDao.insertTicketActivity(ticketActivity);
    resultMap.put("ticketActivity", ticketActivity);
    return "add_success";
  }


}
