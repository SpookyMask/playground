package snake.client.model.comm;

public class User {
	public String name;
	public int wins = 0;
	public int losses = 0;
	
	public User() {}
	
	public User(String name){
		this.name = name;
	}
	
}
