package snake.client;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import snake.client.controller.LobbyController;
import snake.client.controller.MultiplayerController;
import snake.client.controller.SettingsController;
import snake.client.model.comm.GameInfo;
import snake.client.model.comm.User;
import snake.client.model.configs.ClientConfig;
import snake.client.model.configs.Constants;

@Component
class EventListenerForMFastultiplayer {
 
    private static final Logger log = Logger.getLogger(EventListenerForMFastultiplayer.class);
 
    public static int counter;
 
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	if(Constants.fastMulti) {
			User me = LobbyController.getStatsFromServer();
			Application.name = me.name;
			GameInfo[] hosts = LobbyController.getHostsFromServer();
			
			if(hosts != null && hosts.length > 0) {
				String s = Application.serverAddress + "join?name="+Application.name+
						   "&host="+hosts[0].hostName;
				GameInfo gInfo = Application.restTemplate.getForObject(s, GameInfo.class);
				MultiplayerController.delayedStart(gInfo);
			} else {
				SettingsController.postSettingsOnServer(new GameInfo());
				MultiplayerController.waitForHost();
			}
		}
    }
}

@SpringBootApplication
public class Application{
	
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
	
	interface Anarchy{
		public void run();
	}

}