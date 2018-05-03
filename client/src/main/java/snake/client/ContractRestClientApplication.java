package snake.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import snake.client.model.comm.Stats;
import snake.client.model.configs.ClientConfig;

@SpringBootApplication
public class ContractRestClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContractRestClientApplication.class, args);
	}
}

@RestController
class MessageRestController {

	private final RestTemplate restTemplate;

	MessageRestController(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@RequestMapping("/message/{personId}")
	String getMessage(@PathVariable("personId") Long personId) {
		Stats stats = this.restTemplate.getForObject("http://localhost:8000/person/{personId}", Stats.class, ClientConfig.getProperty("player_name"));
		return "Hello " + stats.name;
	}

}
