package snake.client.model.comm;

import snake.client.model.game.Position;

public class Turn {
	public String name;
	public int turnNr;
	public int hostDir, guestDir;
	public int frogX, frogY;
	public long lap;
	public String checksum;
	
	public Turn() {
		
	}
	
	public Turn( String n ) {
		name = n;
		hostDir = 0;
		guestDir = 2;
		frogX = -1;
	}
	
	public Turn( String n, Position frog, long l, String cSum ) {
		name = n;
		if(frog == null)
			frogX = -1;
		else {
			frogX = frog.getX();
			frogY = frog.getY();
		}
		lap = l;
		checksum = cSum;
	}
	
	@Override
	public String toString() {
		String frog = (frogX != -1)? " (" + frogX + "," + frogY + ")": "";
		return turnNr + ". Turn [" + hostDir + ", " + guestDir + frog + "] " + name;
	}

}
