Snake game Server Client Application

1. Built on Lubuntu using Spring Tool Suite, MySQL, mysql-connector-java-5.1.46
2. Used resources:
2.1. Java TCP Sockets and Swing Tutorial - Swing GUI, events
https://www.cise.ufl.edu/~amyles/tutorials/tcpchat/
2.2. socket programming multiple client to one server
https://stackoverflow.com/questions/10131377/socket-programming-multiple-client-to-one-server
2.3. Example to connect to the mysql database in java
https://www.javatpoint.com/example-to-connect-to-the-mysql-database
2.4. A Visual Guide to Layout Managers
https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html#grid
2.5. Building a RESTful Web Service
https://spring.io/guides/gs/rest-service/#initial
2.6. Accessing data with MySQL
https://spring.io/guides/gs/accessing-data-mysql
2.7. Accessing Data with JPA
https://spring.io/guides/gs/accessing-data-jpa/
2.8. Java Apache HttpClient REST (RESTful) client examples
https://alvinalexander.com/java/java-apache-httpclient-restful-client-examples
2.9. JPA - Entity Relationships
https://www.tutorialspoint.com/jpa/jpa_entity_relationships.htm
3. ToDos
*Premature optimization is the root of all evil*
3.2. Proper encapsulation
3.3. Proper package structure
3.4 Board to be Set
3.5. Database
4. Communication protocol
4.1. Structure
    {c/s},{auth/stts/hsts/join/host/strt/over/turn/quit},{data1, data2, ...}
4.1.1. {c/s} - Sender: c for client and s for server
4.1.2. {auth/stts/hsts/join/host/strt/over/turn} - operation type
4.1.3. {data1, data2, ...} - data fields to be tranfered: stats, hosts and settings.
5. Dependencies
org.springframework.boot
spring-boot-starter-web

com.h2database
h2

org.springframework.boot
spring-boot-starter-data-jpa

org.springframework.boot
spring-boot-starter-test

com.jayway.jsonpath
json-path
6. git
git add .
git commit -m "message"