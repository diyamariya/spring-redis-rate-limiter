Implemented a **rate limiter with redis**, which uses sorted sets (ZSET) to efficiently track and expire old requests. 
Spring Boot framework used for Spring MVC to define REST API endpoints here. 

- A user sends an API request (Spring Boot handles the request).
- The interceptor checks Redis to see how many requests the user made in a given time window.
- If the user exceeds the limit, Redis blocks further requests (429 Too Many Requests).
- If the user is within the limit, the request proceeds to the controller.

Used Postman to demonstrate. 
In Postman, make a GET request to the port, in my case http://localhost:8080/api/test

For first 5 requests:
<img width="632" alt="image" src="https://github.com/user-attachments/assets/4946eba0-1a7c-4807-b36c-014b313e8bc7" />

On exceeding 5 requests in 10 seconds, this will be the output:
<img width="635" alt="image" src="https://github.com/user-attachments/assets/b9c1288a-562b-41d0-afc6-a0c5f0482c2f" />
