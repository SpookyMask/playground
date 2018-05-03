package snake.server.model.comm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Stats {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	public String name;
	public int wins = 0;
	public int losses = 0;

	public Stats(String name, int wins, int losses) {
		this.name = name;
		this.wins = wins;
		this.losses = losses;
	}

}
