package snake.client.model.comm;

public class Turn {
	public String hName, name;
	public int hostDir;
	public int dir;
	public boolean endingTurn;
	
    public Turn(){
		
	}
    public Turn(String hName, String name){
    	this.hName = hName;
    	this.name = name;
    }
}
