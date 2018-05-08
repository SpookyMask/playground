package snake.server;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
	
	final static Logger log = Logger.getLogger(Application.class);
	
	@Bean
	public Logger log() {
		return Logger.getLogger(Application.class);
	}
	
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
