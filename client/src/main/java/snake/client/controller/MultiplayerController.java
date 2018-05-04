package snake.client.controller;

import org.springframework.http.ResponseEntity;

import snake.client.Application;
import snake.client.model.comm.GameInfo;
import snake.client.model.comm.Turn;
import snake.client.model.configs.Constants;
import snake.client.model.game.Snake;
import snake.client.view.GameView;

public class MultiplayerController extends SingleplayerController {
	private static MultiplayerController controller = null;
	
	protected MultiplayerController() {
		view = GameView.getInstance();
	}
	
	public static MultiplayerController getInstance() {
		return controller;
	}
	
	protected static MultiplayerController initController(GameInfo gInfo) {
	    return new  MultiplayerController();
	}
	
	@Override
	public void onDirUpdate(int newDir) {
		player.setDir(newDir);
	}
	
	public int turn(int direction) {
		int over = 0;
		opponent.setDir(direction);
		
		if(slot == 0) {
			over = move(player);
			move(opponent);
		} else {
			move(opponent);
			over = move(player);
		}
		
		addFrogMabye();
		
		return over;
	}
	
	public void waitForGuest() {
		do {
			try{
	    	    Thread.sleep(Constants.hostCheckInterval);
	    	} catch(InterruptedException ex) {
	    	    Thread.currentThread().interrupt();
	    	}
			
			String s = Application.serverAddress + "wait?name=" + Application.name;
			GameInfo gInfo = Application.restTemplate.getForObject(s, GameInfo.class);
			if(gInfo != null) {
				controller.start();
				break;
			}
		} while(true);
	}
	
	//join  --  guest joins
	public static void delayedStart(GameInfo gInfo) {
		try{
    	    Thread.sleep(gInfo.startsIn);
    	} catch(InterruptedException ex) {
    	    Thread.currentThread().interrupt();
    	}
		controller.onStart(gInfo);
	}
	
	//wait  --  host waits for guest
	public static void waitForHost() {
		GameInfo gInfo;
		do {
			try{
	    	    Thread.sleep(Constants.hostCheckInterval);
	    	} catch(InterruptedException ex){
	    	    Thread.currentThread().interrupt();
	    	}
			gInfo = getGameInfoFromServer();
			if(gInfo != null) break;
		} while(true);
		try{
    	    Thread.sleep(gInfo.startsIn);
    	} catch(InterruptedException ex){
    	    Thread.currentThread().interrupt();
    	}
		controller.onStart(gInfo);
	}
	
	@Override
	public void run() {
		Turn status;
		do {
			try{
	    	    Thread.sleep(gInfo.turnTimeMS);
	    	} catch(InterruptedException ex){
	    	    Thread.currentThread().interrupt();
	    	}
	    	view.repaint();
	    	gInfo.turnTimeMS -= gInfo.decreaseTimeMS;
	    	status = getEndTurn();
	    	if(status.over) {
	    		break;
	    	} else if(status.penalty > 0) {
	    		gInfo.turnTimeMS -= status.penalty;
	    	}
	    		
		} while(turn(status.oppDir) != -1);
		
		controller.view.setVisible(false);
		MenuController.activate();
	}
	
	public static GameInfo getGameInfoFromServer() {
		String s = Application.serverAddress + "wait?name="+Application.name;
		return Application.restTemplate.getForObject(s, GameInfo.class);
	}
	
	public static void putDirToServer(int dir) {
		String s = Application.serverAddress + "aim?name="+Application.name+
				   "&dir="+(new Integer(dir)).toString();
		Application.restTemplate.getForObject(s, ResponseEntity.class);
	}
	
	public static Turn postEndTurn(Turn endTurn) {
		String s = Application.serverAddress + "endturn?name="+Application.name;
		return Application.restTemplate.postForObject(s, endTurn, Turn.class);
	}
	
	public static Turn getEndTurn() {
		String s = Application.serverAddress + "endturn?name="+Application.name+
				   "&dir="+controller.player.getDir();
		return Application.restTemplate.getForObject(s, Turn.class);
	}
	
	public static void putOverToServer() {
		String s = Application.serverAddress + "aim?name="+Application.name;
		Application.restTemplate.getForObject(s, ResponseEntity.class);
	}
}
