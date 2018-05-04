package snake.client.model.comm;

public class Turn {
	public String name;
	public int dir;
	public int frogX, frogY;
	public long penalty;
	public boolean waiting = false;
	public boolean over = false;
	
    public Turn(){
		
	}
	
    public Turn(int dir, int penalty){
		this.dir = dir;
		this.penalty = penalty;
	}
}
