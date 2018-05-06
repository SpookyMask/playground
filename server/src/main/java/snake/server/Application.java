package snake.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

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
    	log.trace("yuyu");
    	
    	try {
			System.setIn(new FileInputStream("snake.log"));
		} catch (FileNotFoundException e) {
			if(log.isDebugEnabled()) log.debug("Failed to set console to snake.log");
			e.printStackTrace();
		}
    	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    	try {
			String text = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        SpringApplication.run(Application.class, args);
    }
}
