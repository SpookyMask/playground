package snake.server.model.comm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Turn {
	
	@Id
	@GeneratedValue
	public int id;
	
	public String name;
	public int hostDir, guestDir;
	public int frogX, frogY;
	public long penalty;
	public boolean endTurn;
	public boolean over = false;
	public boolean pending = false;
	
	
	public Turn() {
		
	}
	
	public Turn( String name, int hostDir, int guestDir, int frogX, int frogY, long penalty, boolean endTurn,
			boolean over, boolean pending) {
		super();
		this.name = name;
		this.hostDir = hostDir;
		this.guestDir = guestDir;
		this.frogX = frogX;
		this.frogY = frogY;
		this.penalty = penalty;
		this.endTurn = endTurn;
		this.over = over;
		this.pending = pending;
	}
	
	public Turn pending() {
		return new Turn( name,  hostDir,  guestDir,  frogX,  frogY,  penalty,  endTurn,
				 over,  true);
	}

	@Override
	public String toString() {
		return "Turn [name=" + name + ", hostDir=" + hostDir + ", guestDir=" + guestDir + ", frogX=" + frogX
				+ ", frogY=" + frogY + ", penalty=" + penalty + ", endTurn=" + endTurn + ", over=" + over + "]";
	}

}
