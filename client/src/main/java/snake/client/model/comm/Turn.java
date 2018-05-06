package snake.client.model.comm;

public class Turn {
	public int id;	
	public String name;
	public int hostDir, guestDir;
	public int frogX, frogY;
	public long penalty;
	public boolean endTurn;
	public boolean over = false;
	public boolean pending = false;
	
	@Override
	public String toString() {
		return "Turn [name=" + name + ", hostDir=" + hostDir + ", guestDir=" + guestDir + ", frogX=" + frogX
				+ ", frogY=" + frogY + ", penalty=" + penalty + ", endTurn=" + endTurn + ", over=" + over + "]";
	}

}
