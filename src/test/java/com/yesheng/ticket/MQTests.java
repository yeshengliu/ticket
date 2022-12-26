package com.yesheng.ticket;

import com.yesheng.ticket.mq.RocketMQService;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MQTests {

  @Autowired
  RocketMQService rocketMQService;

  @Test
  public void sendMQTest() throws Exception {
    rocketMQService.sendMessage("test", "Hello World!" + new Date().toString());
  }
}
