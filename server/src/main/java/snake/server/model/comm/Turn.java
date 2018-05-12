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
	public String checksum;
	public long sync;
	
	private static enum Status{OK, IDLE, ОVER};
	public Status status = Status.OK;
	public boolean isOK() {
		return status == Status.ОVER;
	}
	public boolean isOver() {
		return status == Status.ОVER;
	}
	public boolean isIdle() {
		return status == Status.IDLE;
	}
	public void refresh() {
		status = Status.OK;
	}
	public void idle() {
		status = Status.IDLE;
	}
	public void over() {
		status = Status.ОVER;
	}
	
	public Turn() {
		
	}
	
	public Turn(String n) {
		name = n;
	}
	
	public Turn( boolean init ) {
		name = "server";
		hostDir = 0;
		guestDir = 2;
		frogX = -1;
	}
	
	public Turn clone(){
		try {
			return (Turn) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}  
	
	public Turn(String name, int frogX, int frogY) {
		this.name = name;
		this.frogX = frogX;
		this.frogY = frogY;
	}

	@Override
	public String toString() {
		String frog = (frogX != -1)? " (" + frogX + "," + frogY + ")": "";
		return turnNr + ". Turn [" + hostDir + ", " + guestDir + frog + "] " + name+"("+status+")";
	}

}
