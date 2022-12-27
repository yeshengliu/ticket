package com.yesheng.ticket.services;

import com.yesheng.ticket.db.dao.TicketActivityDao;
import com.yesheng.ticket.db.dao.TicketDao;
import com.yesheng.ticket.db.po.Ticket;
import com.yesheng.ticket.db.po.TicketActivity;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class ActivityHtmlPageService {

  @Autowired
  private TemplateEngine templateEngine;

  @Autowired
  private TicketActivityDao ticketActivityDao;

  @Autowired
  private TicketDao ticketDao;

  public void createActivityHtml(long ticketActivityId) {
    PrintWriter writer = null;
    try {
      TicketActivity ticketActivity = ticketActivityDao.queryTicketActivityById(ticketActivityId);
      Ticket ticket = ticketDao.queryTicketById(ticketActivity.getTicketId());
      // Web components
      Map<String, Object> resultMap = new HashMap<>();
      resultMap.put("ticketActivity", ticketActivity);
      resultMap.put("ticket", ticket);
      resultMap.put("salePrice", ticketActivity.getSalePrice());
      resultMap.put("originalPrice", ticketActivity.getOriginalPrice());
      resultMap.put("ticketId", ticketActivity.getTicketId());
      resultMap.put("ticketName", ticket.getTicketName());
      resultMap.put("ticketDesc", ticket.getTicketDesc());
      // Create thymeleaf context
      Context context = new Context();
      // Put web components into context
      context.setVariables(resultMap);
      // Create static webpage
      File file = new File("src/main/resources/templates/" + "ticket_item_" + ticketActivityId + ".html");
      writer = new PrintWriter(file);
      templateEngine.process("ticket_item", context, writer);
    } catch (Exception e) {
      log.error(e.toString());
      log.error("Failed to generate static webpage: " + ticketActivityId);
    } finally {
      {
        if (writer != null) {
          writer.close();
        }
      }
    }
  }
}
