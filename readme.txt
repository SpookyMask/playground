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
2.19. BAELDUNG Spring Tutorial
http://www.baeldung.com/spring-tutorial
2.20 JUnit
https://junit.org/junit4/faq.html
2.21. Spring Common application properties
https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
2.22. Spring Creating a Multi Module Project
https://spring.io/guides/gs/multi-module/
2.23.Spring Boot Dashboard 
https://spring.io/blog/2015/10/08/the-spring-boot-dashboard-in-sts-part-1-local-boot-apps
https://github.com/spring-projects/spring-boot/issues/7179
2.24. @Value
2.25. CloudFoundry
http://joshlong.com/jl/blogPost/getting_started_with_cloud_foundry_for_java_and_spring_developers.html
https://stackoverflow.com/questions/29412072/how-to-access-spring-boot-jmx-remotely
2.26. Many tutorials
http://www.java2s.com/Code/JavaAPI/javax.swing.table/newDefaultTableModelObjectdataObjectcolumnNames.htm
2.27. 
3. ToDos
*Premature optimization is the root of all evil*
3.1. Add game results, writing to database, update userStats, leave game if other player not present
3.2. Read about Autoboxing, JUnit
org.springframework.boot
spring-boot-starter-web
6. Source code
6.1. Search for file names
grep --include=\*.{java} -rnw ' /home/sh_home/Documents/Kalin/Java/workspace/repository/' -e "gs-rest-service" 
6.2. Mysql syntax
mysql -u root -p
>>> root
show databases;
use db_snake;x
show tables;
select * from users;
truncate table;             //empty
drop database db_snake;
6.3. Application accessible URLs
//Browser URLs for testing
http://localhost:8080/stats?name=random
6.4. 
    @GetMapping("endturn")
    public Turn endturn(@PathVariable("name") String name) {
    	Game game = runningGames.get(name);
    	return game.getCurrentTurn();
    }
7. 


