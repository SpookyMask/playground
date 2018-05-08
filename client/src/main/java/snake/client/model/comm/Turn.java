package snake.client.model.comm;

public class Turn {
	public String name;
	public int turnNr;
	public int hostDir, guestDir;
	public int frogX, frogY;
	
	public Turn() {
		
	}
	
	public Turn( String n ) {
		name = n;
		hostDir = 0;
		guestDir = 2;
		frogX = -1;
	}
	
	@Override
	public String toString() {
		String frog = (frogX != -1)? " (" + frogX + "," + frogY + ")": "";
		return turnNr + ". Turn [" + hostDir + ", " + guestDir + frog + "] " + name;
	}

}
