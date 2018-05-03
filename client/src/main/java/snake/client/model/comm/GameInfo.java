package snake.client.model.comm;

import snake.client.model.configs.Constants;

public class GameInfo {
	public Host host;
	public String name;
	public long startsIn;
	
	public GameInfo(){
		
	}
	
	public GameInfo(Host host, String name) {
		this.host = host;
		this.name = name;
	}
	
//	//make sure the host knows when the game starts
//	public long startsIn() {
//		return scheduledStart - System.currentTimeMillis();
//	}
}
