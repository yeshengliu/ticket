package com.yesheng.ticket.web;

import com.alibaba.fastjson.JSON;
import com.yesheng.ticket.db.dao.OrderDao;
import com.yesheng.ticket.db.dao.TicketActivityDao;
import com.yesheng.ticket.db.dao.TicketDao;
import com.yesheng.ticket.db.po.Order;
import com.yesheng.ticket.db.po.Ticket;
import com.yesheng.ticket.db.po.TicketActivity;
import com.yesheng.ticket.services.TicketActivityService;
import com.yesheng.ticket.util.RedisService;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class TicketActivityController {

  @Autowired
  private TicketActivityDao ticketActivityDao;

  @Autowired
  private TicketDao ticketDao;

  @Autowired
  TicketActivityService ticketActivityService;

  @Autowired
  OrderDao orderDao;

  @Resource
  private RedisService redisService;

  @RequestMapping("/item/{ticketActivityId}")
  public String itemPage(Map<String, Object> resultMap, @PathVariable long ticketActivityId) {
    TicketActivity ticketActivity;
    Ticket ticket;

    /*
     * 1. Look up ticket activity in redis
     * if not in redis, load ticket activity from db
     */
    String ticketActivityInfo = redisService.getValue("ticketActivity:" + ticketActivityId);
    if (StringUtils.isNotEmpty(ticketActivityInfo)) {
      log.info("Ticket activity load from redis: " + ticketActivityInfo);
      ticketActivity = JSON.parseObject(ticketActivityInfo, TicketActivity.class);
    } else {
      log.info("Ticket activity load from db: " + ticketActivityId);
      ticketActivity = ticketActivityDao.queryTicketActivityById(ticketActivityId);
    }

    /*
     * 2. Look up ticket in redis
     * if not in redis, load ticket from db
     */
    String ticketInfo = redisService.getValue("ticket:" + ticketActivity.getTicketId());
    if (StringUtils.isNotEmpty(ticketInfo)) {
      log.info("Ticket load from redis: " + ticketInfo);
      ticket = JSON.parseObject(ticketInfo, Ticket.class);
    } else {
      log.info("Ticket load from db: " + ticketActivity.getTicketId());
      ticket = ticketDao.queryTicketById(ticketActivity.getTicketId());
    }

    /*
     * 3. Put ticket activity information to web components
     */
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

  @RequestMapping("/ticket/buy/{userId}/{ticketActivityId}")
  public ModelAndView ticketCommodity(@PathVariable long userId, @PathVariable long ticketActivityId) {
    boolean stockValidateResult = false;

    ModelAndView modelAndView = new ModelAndView();
    try {
      /*
       * 1. Verify if the user has placed the order
       * user cannot place multiple orders
       */
      if (redisService.isInLimitMember(ticketActivityId, userId)) {
        modelAndView.addObject("resultInfo", "Sorry, you have already purchased this item");
        modelAndView.setViewName("ticket_result");
        return modelAndView;
      }

      /*
       * 2. Verify if there is available stock
       */
      stockValidateResult = ticketActivityService.ticketStockValidator(ticketActivityId);
      if (stockValidateResult) {
        Order order = ticketActivityService.createOrder(ticketActivityId, userId);
        modelAndView.addObject("resultInfo", "Successfully created order, order ID: " + order.getOrderNo());
        modelAndView.addObject("orderNo", order.getOrderNo());
        // add the user into purchased group
        redisService.addLimitMember(ticketActivityId, userId);
      } else {
        modelAndView.addObject("resultInfo", "Sorry, the item is sold out");
      }
    } catch (Exception e) {
      log.error("System error" + e.toString());
      modelAndView.addObject("resultInfo", "Failed to create order");
    }
    modelAndView.setViewName("ticket_result");
    return modelAndView;
  }

  @RequestMapping("/ticket/orderQuery/{orderNo}")
  public ModelAndView orderQuery(@PathVariable String orderNo) {
    log.info("Order Lookup, Order No: " + orderNo);
    Order order = orderDao.queryOrder(orderNo);
    ModelAndView modelAndView = new ModelAndView();

    if (order != null) {
      modelAndView.setViewName("order");
      modelAndView.addObject("order", order);
      TicketActivity ticketActivity = ticketActivityDao.queryTicketActivityById(order.getTicketActivityId());
      modelAndView.addObject("ticketActivity", ticketActivity);
    } else {
      modelAndView.setViewName("order_wait");
    }
    return modelAndView;
  }

  @RequestMapping("/ticket/payOrder/{orderNo}")
  public String payOrder(@PathVariable String orderNo) throws Exception {
    ticketActivityService.payOrderProcess(orderNo);
    return "redirect:/ticket/orderQuery/" + orderNo;
  }
}
