package org.springframework.SnakeGame;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Hello world!
 *
 */




public class App 
{
    public static void main( String[] args )
    {
    	while(true) {
	        RestTemplate restTemplate = new RestTemplate();
	        ResponseEntity<A> response
	        = restTemplate.getForEntity("http://localhost:8080/obj", A.class);
	        A quote = restTemplate.getForObject("http://localhost:8080/obj", A.class);
	        System.out.println(quote.i + ":" + quote.s);
    	}
    }
}
