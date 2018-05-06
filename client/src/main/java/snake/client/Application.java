package snake.client;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import snake.client.model.configs.ClientConfig;

@SpringBootApplication
public class Application{
	
//	@Autowired
	public static RestTemplate restTemplate;
	
	public static String serverAddress;
	public static String name;

	final public static Logger log = Logger.getLogger(Application.class);
	
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
		restTemplate = builder.build();
		return restTemplate;
	}
	
//	@Bean
//	public Logger log() {
//		return Logger.getLogger(Application.class);
//	}

}