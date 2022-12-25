package com.yesheng.ticket.util;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

  @Autowired
  private JedisPool jedisPool;

  public void setValue(String key, Long value) {
    Jedis jedisClient = jedisPool.getResource();
    jedisClient.set(key, value.toString());
    jedisClient.close();
  }

  public String getValue(String key) {
    Jedis jedisClient = jedisPool.getResource();
    String value = jedisClient.get(key);
    jedisClient.close();
    return value;
  }

  public boolean stockDeductValidator(String key) {
    try (Jedis jedisClient = jedisPool.getResource()) {
      String script = "if redis.call('exists',KEYS[1]) == 1 then\n" +
          "                 local stock = tonumber(redis.call('get', KEYS[1]))\n" +
          "                 if( stock <=0 ) then\n" +
          "                    return -1\n" +
          "                 end;\n" +
          "                 redis.call('decr',KEYS[1]);\n" +
          "                 return stock - 1;\n" +
          "             end;\n" +
          "             return -1;";
      Long stock = (Long) jedisClient.eval(script, Collections.singletonList(key), Collections.emptyList());
      if (stock < 0) {
        System.out.println("Out of stock");
        return false;
      } else {
        System.out.println("Congratulations, your order is complete");
      }
      return true;
    } catch (Throwable throwable) {
      System.out.println("Failed to deduct stock: " + throwable.toString());
      return false;
    }
  }
}
