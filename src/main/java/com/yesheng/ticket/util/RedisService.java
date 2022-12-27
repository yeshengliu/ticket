package com.yesheng.ticket.util;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
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

  public void revertStock(String key) {
    Jedis jedisClient = jedisPool.getResource();
    jedisClient.incr(key);
    jedisClient.close();
  }

  public boolean isInLimitMember(long activityId, long userId) {
    Jedis jedisClient = jedisPool.getResource();
    boolean sismember = jedisClient.sismember("ticketActivity_users:" + activityId, String.valueOf(userId));
    jedisClient.close();
    log.info("userId:{} activityId:{} in_purchased_group:{}", userId, activityId, sismember);
    return sismember;
  }

  public void addLimitMember(long activityId, long userId) {
    Jedis jedisClient = jedisPool.getResource();
    jedisClient.sadd("ticketActivity_users" + activityId, String.valueOf(userId));
    jedisClient.close();
  }

  public void removeLimitMember(long activityId, Long userId) {
    Jedis jedisClient = jedisPool.getResource();
    jedisClient.srem("ticketActivity_users" + activityId, String.valueOf(userId));
    jedisClient.close();
  }
}
