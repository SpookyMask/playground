package snake.client.model.comm;

import snake.client.model.game.Position;

public class Turn {
	public String name;
	public int turnNr;
	public int hostDir, guestDir;
	public int frogX, frogY;
	
	public String gState;
	
	public Turn() {
		
	}
	
	public Turn( String n ) {
		name = n;
		hostDir = 0;
		guestDir = 2;
		frogX = -1;
	}
	
	public Turn( String n, Position frog, String gameState ) {
		name = n;
		if(frog == null)
			frogX = -1;
		else {
			frogX = frog.getX();
			frogY = frog.getY();
		}
		gState = gameState;
	}
	
	@Override
	public String toString() {
		String frog = (frogX != -1)? " (" + frogX + "," + frogY + ")": "";
		return turnNr + ". Turn [" + hostDir + ", " + guestDir + frog + "] " + name;
	}

}
