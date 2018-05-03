package snake.client.controller;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.ResponseEntity;

import snake.client.Application;
import snake.client.model.comm.GameInfo;
import snake.client.model.comm.Host;
import snake.client.model.comm.Settings;
import snake.client.model.comm.Stats;
import snake.client.model.comm.Turn;
import snake.client.model.configs.ClientConfig;
import snake.client.model.configs.Constants;
import snake.client.model.game.Snake;
import snake.client.view.GameView;
import snake.client.view.LobbyView;

public class MultiplayerController extends SingleplayerController {
	private static MultiplayerController controller = null;
	private GameInfo gInfo;
	
	private MultiplayerController(GameInfo g) {
		gInfo = g;
	    settings = g.host.settings;
	    slot = g.host.settings.slot;
	    player = new Snake(slot);
	    frogs = new HashSet<>();
	    opponent = new Snake((slot+1)%2);
		GameView.activate(controller.frogs, controller.player, controller.opponent, 
				          controller.settings.sizeN, controller.settings.sizeM);
		
	}
	
	public static MultiplayerController getInstance() {
		return controller;
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
		controller = new MultiplayerController(gInfo);
		try{
    	    Thread.sleep(controller.gInfo.startsIn);
    	} catch(InterruptedException ex) {
    	    Thread.currentThread().interrupt();
    	}
		controller.start();
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
		controller = new MultiplayerController(gInfo);
		controller.start();
	}
	
	@Override
	public void run() {
		do {
			try{
	    	    Thread.sleep(settings.turnTimeMS);
	    	} catch(InterruptedException ex){
	    	    Thread.currentThread().interrupt();
	    	}
	    	view.repaint();
	    	settings.turnTimeMS -= settings.decreaseTimeMS;
	    	
	    	String s = Application.serverAddress + "stats?name="+Application.name;
			Stats stats = Application.restTemplate.getForObject(s, Stats.class);
	    	
		} while(turn() != -1);
		view.setVisible(false);
		
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
	
	public static void putOverToServer() {
		String s = Application.serverAddress + "aim?name="+Application.name;
		Application.restTemplate.getForObject(s, ResponseEntity.class);
	}
}
