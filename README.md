# ticket

This project emulates a ticket sale website that is able to handle huge amount of requests happening at a short timeframe (after ticket on sale)

## Tech Stack to make it happen
* Spring Boot, MySQL
* Redis (caching user requests)
* RocketMQ (respond user requests)
* Sentinel (protect server by blocking DDOS)

## Pressure Test by JMeter
* Environment: 1000 threads, 50 requests per thread
* Result: 1767.7 requests per second

![jmeter result](jmeter/Screenshot%202022-12-27%20at%206.10.00%20AM.png)