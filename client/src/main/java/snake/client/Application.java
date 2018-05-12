package snake.client;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.client.RestTemplate;

import snake.client.model.configs.ClientConfig;

@SpringBootApplication
public class Application{
	
	public static RestTemplate restTemplate = new RestTemplate();
	
	public static String serverAddress;
	public static String name;
	
	public static boolean simulation = false;

	final public static Logger log = Logger.getLogger(Application.class);
	
	public static void main(String args[]) {
		
		ClientConfig.read();
		name = ClientConfig.getProperty("player_name");
		serverAddress = "http://" + ClientConfig.getProperty("server_ip_address") +
		                ":" + ClientConfig.getProperty("server_port") + "/";
		
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class);
        builder.headless(false).run(args);
        
	}

}