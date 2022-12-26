package com.yesheng.ticket.util;

public class SnowFlake {

  private final static long START_STAMP = 1480166465631L;

  private final static long SEQUENCE_BIT = 12;
  private final static long MACHINE_BIT = 5;
  private final static long DATACENTER_BIT = 5;

  private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
  private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
  private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);

  private final static long MACHINE_LEFT = SEQUENCE_BIT;
  private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
  private final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

  private long datacenterId;
  private long machineId;
  private long sequence = 0L;
  private long lastStamp = -1L;

  public SnowFlake(long datacenterId, long machineId) {
    if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
      throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
    }
    if (machineId > MAX_MACHINE_NUM || machineId < 0) {
      throw new IllegalArgumentException(
          "machineId can't be greater than MAX_MACHINE_NUM or less than 0");
    }
    this.datacenterId = datacenterId;
    this.machineId = machineId;
  }

  public synchronized long nextId() {
    long currStamp = getNewStamp();
    if (currStamp < lastStamp) {
      throw new RuntimeException("Clock moved backwards. Refusing to generate id");
    }
    if (currStamp == lastStamp) {
      sequence = (sequence + 1) & MAX_SEQUENCE;
      if (sequence == 0L) {
        currStamp = getNextMill();
      }
    } else {
      sequence = 0L;
    }

    lastStamp = currStamp;

    return (currStamp - START_STAMP) << TIMESTAMP_LEFT
        | datacenterId << DATACENTER_LEFT
        | machineId << MACHINE_LEFT
        | sequence;
  }

  private long getNextMill() {
    long mill = getNewStamp();
    while (mill < lastStamp) {
      mill = getNewStamp();
    }
    return mill;
  }

  private long getNewStamp() {
    return System.currentTimeMillis();
  }

  public static void main(String[] args) {
    SnowFlake snowFlake = new SnowFlake(2, 1);

    long start = System.currentTimeMillis();
    for (int i = 0; i < 1000000; i++) {
      System.out.println(snowFlake.nextId());
    }

    System.out.println("Total time consumed: " + (System.currentTimeMillis() - start));
  }
}
