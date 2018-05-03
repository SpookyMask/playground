package snake.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Test {
	public static RestTemplate restTemplate;
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
		return restTemplate;
	}
	
	public static void main(String args[]) {
		SpringApplication.run(Test.class, args);
	}
}
