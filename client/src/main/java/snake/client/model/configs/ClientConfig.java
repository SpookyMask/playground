package snake.client.model.configs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

public class ClientConfig {
	private static final String defaultConfig = "server_ip_address = localhost\nserver_port = 8080 \nplayer_name = Newbie";
	private static final Map<String, String> properties = new HashMap<>();
	private static final Path file = Paths.get("config.txt");
	
	private static final Charset charset = Charset.forName("US-ASCII");
	
	public static void write() { write(null); }
	
	public static void write(String s) {
		if( s == null) s = defaultConfig;
		try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
		    writer.write(s, 0, s.length());
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
	}
	
	public static void read() {
		try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		        String[] prop = line.split("=");
		        properties.put(prop[0].trim(), prop[1].trim());
		    }
		} catch (java.nio.file.NoSuchFileException e) {
			System.err.format("IOException: %s%n", e);
			System.out.println("Creating defaut config");
			write();
			read();
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
	}
	
	public static String getProperty(String name) {
		return properties.get(name);
	}
	
	public static void main(String[] args) {
		//write();
		read();
	}
}
