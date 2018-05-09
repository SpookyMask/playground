package snake.server.model.comm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="turns")
public class Turn {
	
	@Id
	@GeneratedValue
	@JsonIgnore
	public int id;

	public String name;
	public int turnNr;
	public int hostDir, guestDir;
	public int frogX, frogY;
	public long lap;
	public String checksum;
	
	public Turn() {
		
	}
	
	public Turn( boolean init ) {
		name = "server";
		hostDir = 0;
		guestDir = 2;
		frogX = -1;
	}
	
	

	public Turn(Turn turn) {
		this.name = turn.name;
		this.turnNr = turn.turnNr;
		this.hostDir = turn.hostDir;
		this.guestDir = turn.guestDir;
		this.frogX = turn.frogX;
		this.frogY = turn.frogY;
		this.lap = turn.lap;
		this.checksum = turn.checksum;
	}
	
	public Turn(String name, int frogX, int frogY) {
		this.name = name;
		this.frogX = frogX;
		this.frogY = frogY;
	}

	@Override
	public String toString() {
		String frog = (frogX != -1)? " (" + frogX + "," + frogY + ")": "";
		return turnNr + ". Turn [" + hostDir + ", " + guestDir + frog + "] " + name;
	}

}
