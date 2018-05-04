package snake.server.model.comm;

import com.fasterxml.jackson.annotation.JsonIgnore;

import snake.server.model.configs.Constants;

public class GameInfo {
		
	public String hostName, guestName;

	@JsonIgnore
	public long startsTimeStamp;
	
	public long startsIn;
	
	public int sizeN = Constants.sizeN, sizeM = Constants.sizeM;
	
	public boolean noBorder = Constants.noBorder;
	
	public int turnTimeMS = Constants.turnTimeMS;
	
	public int decreaseTimeMS = Constants.decreaseTimeMS;
	
	public GameInfo(){
	}
	
	public void setTimeStamp() {
		startsTimeStamp = System.currentTimeMillis() + Constants.gameStartDelay; 
		startsIn = Constants.gameStartDelay;
		System.out.println("Game " + hostName + " vs. " + guestName + " starts in:" + startsIn);
	}
	
	public void updateStartIn() {
		startsIn = startsTimeStamp - System.currentTimeMillis();
		System.out.println("Game " + hostName + " vs. " + guestName + " : Host notified, game starts in:" + startsIn);
	}
	
}
