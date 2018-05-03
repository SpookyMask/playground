package snake.client.model.comm;

public class Stats {
	public int id;	
	public String name;
	public int wins = 0;
	public int losses = 0;
	
	public Stats() {}

	public Stats(int id, String name, int wins, int losses) {
		this.id = id;
		this.name = name;
		this.wins = wins;
		this.losses = losses;
	}
}
