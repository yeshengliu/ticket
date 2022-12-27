package com.yesheng.ticket;

import com.yesheng.ticket.services.TicketActivityService;
import com.yesheng.ticket.util.RedisService;
import java.util.UUID;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Test
  public void stockDeductValidatorTest() {
    boolean result = redisService.stockDeductValidator("stock:19");
    System.out.println("result:" + result);
    String stock = redisService.getValue("stock:19");
    System.out.println("stock:" + stock);
  }

  @Autowired
  TicketActivityService ticketActivityService;

  @Test
  public void pushTicketInfoToRedisTest() {
    ticketActivityService.pushTicketInfoToRedis(31);
  }

  @Test
  public void testConcurrentAddLock() {
    for (int i = 0; i < 10; i++) {
      String requestId = UUID.randomUUID().toString();
      System.out.println(redisService.getDistributedLock("A", requestId, 1000));
    }
  }

  @Test
  public void testConcurrent() {
    for (int i = 0; i < 10; i++) {
      String requestId = UUID.randomUUID().toString();
      System.out.println(redisService.getDistributedLock("A", requestId, 1000));
      redisService.releaseDistributedLock("A", requestId);
    }
  }
}
