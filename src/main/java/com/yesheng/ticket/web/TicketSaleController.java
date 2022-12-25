package com.yesheng.ticket.web;

import com.yesheng.ticket.services.TicketActivityService;
import com.yesheng.ticket.services.TicketSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TicketSaleController {

  @Autowired
  private TicketSaleService ticketSaleService;

  @Autowired
  private TicketActivityService ticketActivityService;

  @ResponseBody
  @RequestMapping("/ticket/{ticketActivityId}")
  public String sale(@PathVariable long ticketActivityId) {
    boolean result = ticketActivityService.ticketStockValidator(ticketActivityId);
    return result ? "Congratulations, your order is complete" : "Out of stock";
  }
}
