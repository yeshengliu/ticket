package com.yesheng.ticket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.yesheng.ticket.db.mappers")
@ComponentScan(basePackages = {"com.yesheng"})
public class TicketApplication {

  public static void main(String[] args) {
    SpringApplication.run(TicketApplication.class, args);
  }

}
