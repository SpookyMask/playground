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
2.10. Spring Boot + Spring Data JPA + Oracle example
https://www.mkyong.com/spring-boot/spring-boot-spring-data-jpa-oracle-example/
2.11. Spring Boot samples by Netgloo - git repository
https://github.com/SpookyMask/spring-boot-samples
2.12. 
http://www.baeldung.com/the-persistence-layer-with-spring-and-jpa
2.13. CrudRepository
2.14. Json
https://www.leveluplunch.com/java/tutorials/014-post-json-to-spring-rest-webservice/
2.15. 
http://www.baeldung.com/rest-template
2.15.1. Logging required by RestTemplate
http://commons.apache.org/proper/commons-logging/download_logging.cgi
2.16. Timer Tutorial
https://alvinalexander.com/source-code/java/java-timertask-timer-and-scheduleatfixedrate-example
2.17. Spring RESTFul Client – RestTemplate Example
https://howtodoinjava.com/spring/spring-restful/spring-restful-client-resttemplate-example/
2.18. Logging
https://www.tutorialspoint.com/log4j/log4j_logging_levels.htm
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
6. Source code
6.1. Search for file names
grep --include=\*.{java} -rnw ' /home/sh_home/Documents/Kalin/Java/workspace/repository/' -e "gs-rest-service" 
6.2. Mysql syntax
mysql -u root -p
>>> root
show databases;
use db_snake;
show tables;
select * from users;
truncate table;             //empty
drop database db_snake;
6.3. Application accessible URLs
//Browser URLs for testing
http://localhost:8080/stats?name=random


