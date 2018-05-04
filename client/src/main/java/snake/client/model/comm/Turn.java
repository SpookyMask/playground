package snake.client.model.comm;

public class Turn {
	public int oppDir;
	public int penalty;
	public boolean waiting;
	public boolean over = false;
	
    public Turn(){
		
	}
	
    public Turn(int oppDir, int penalty){
		this.oppDir = oppDir;
		this.penalty = penalty;
	}
}
