package snake.server.model.comm;

public class Turn {
	public int oppDir;
	public long penalty;
	public boolean waiting = false;
	public boolean over = false;
	
    public Turn(){
		
	}
	
    public Turn(int oppDir, long penalty){
		this.oppDir = oppDir;
		this.penalty = penalty;
	}
}
