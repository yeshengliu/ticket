package com.yesheng.ticket;

import com.yesheng.ticket.util.RedisService;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTests {
  @Resource
  private RedisService redisService;

  @Test
  public void stockTest() {
    redisService.setValue("stock:19", 10L);
  }

  @Test
  public void getStockTest() {
    String stock = redisService.getValue("stock:19");
    System.out.println(stock);
  }
}
