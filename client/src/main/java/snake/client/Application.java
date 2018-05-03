package snake.client;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import snake.client.model.configs.ClientConfig;
import snake.client.view.MenuView;

//@Component
//@ComponentScan
@SpringBootApplication
public class Application{
	
//	@Autowired
//	private MenuView menu;
	public static RestTemplate restTemplate;
	public static String serverAddress;
	
	public static int id;
	public static String name;

	public static final Logger log = LoggerFactory.getLogger(Application.class);
	
	public static void main(String args[]) {
		ClientConfig.read();
		name = ClientConfig.getProperty("player_name");
		serverAddress = "http://" + ClientConfig.getProperty("server_ip_address") +
		                ":" + ClientConfig.getProperty("server_port") + "/";
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class);
        builder.headless(false).run(args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
		return restTemplate;
	}

}