package snake.client.model.comm;

import java.util.concurrent.ThreadLocalRandom;

import snake.client.model.configs.Constants;

public class GameInfo {
	public String hostName, name;
	public long startsIn = Constants.defaultDelay;
	public int sizeN = Constants.sizeN, sizeM = Constants.sizeM;
	public boolean noBorder = Constants.noBorder;
	public int turnTimeMS = Constants.turnTimeMS;
	public int decreaseTimeMS = Constants.decreaseTimeMS;
	public int hostSlot = ThreadLocalRandom.current().nextInt(0, 2 );
	
	public GameInfo(){
	}
	
}
