package snake.client.model.comm;

import snake.client.model.game.Position;

public class Turn {
	
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
	
	public Turn( String n ) {
		name = n;
		hostDir = 0;
		guestDir = 2;
		frogX = -1;
	}
	
	public Turn( String n, Position frog, String cSum, long dNextTick ) {
		name = n;
		if(frog == null)
			frogX = -1;
		else {
			frogX = frog.getX();
			frogY = frog.getY();
		}
		checksum = cSum;
		sync = dNextTick;
	}
	
	public Turn( String n, int hDir, int gDir, Position frog, long dNextTick ) {
		name = n;
		hostDir = hDir;
		guestDir = gDir;
		if(frog == null)
			frogX = -1;
		else {
			frogX = frog.getX();
			frogY = frog.getY();
		}
		sync = dNextTick;
	}
	
	@Override
	public String toString() {
		String frog = (frogX != -1)? " (" + frogX + "," + frogY + ")": "";
		return turnNr + ". Turn [" + hostDir + ", " + guestDir + frog + "] " + name;
	}

}
