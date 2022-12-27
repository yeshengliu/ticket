# ticket

This project emulates a ticket sale website that is able to handle huge amount of requests happening at a short timeframe (after ticket on sale)

## Tech Stack to make it happen
* Spring Boot, MySQL
* Redis (caching user requests)
* RocketMQ (respond user requests)
* Sentinel (protect server by blocking DDOS)
