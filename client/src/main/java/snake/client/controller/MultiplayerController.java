package snake.client.controller;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import snake.client.Application;
import snake.client.model.comm.GameInfo;
import snake.client.model.comm.Turn;
import snake.client.model.configs.Constants;
import snake.client.model.game.Position;
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
	
	public static void onStart(GameInfo gInfo) {
		controller = new MultiplayerController();
		boolean isHost = Application.name.equals(gInfo.hostName);
		int slot = isHost? 0 : 1;
		controller.view.setTitle("Game(" + gInfo.hostName + " vs. " + gInfo.guestName + ")");
		controller.view.setVisible(true);
		Position.sizeN = gInfo.sizeN;
		Position.sizeM = gInfo.sizeM;
	    Snake.noBorder = gInfo.noBorder;
	    if(Application.name.equals(gInfo.hostName)) controller.slot = gInfo.hostSlot;
	    else controller.slot = slot;
	    controller.frogsDrop = gInfo.sizeN * gInfo.sizeM * 3 / 40;
	    controller.gInfo = gInfo;
	    controller.player = new Snake(slot);
		controller.opponent = new Snake((slot+1)%2);
	    controller.frogs = new HashSet<>();
		GameView.activate(controller.frogs, controller.player, controller.opponent, 
				          controller.gInfo.sizeN, controller.gInfo.sizeM);
		controller.start();
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
		onStart(gInfo);
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
		onStart(gInfo);
	}
	
	// Players wait for the end of turn. When the first of them calls <<end of turn>>,
	// one moves and updates the game state. The second player calls <<end of turn>> and...
	
	//player receives info for last opponent turn if any.
	//if no last opponent turn: player turn and update game state.
	
	@Override
	public void run() {
		Turn status;
		Turn current = new Turn();
		current.name = Application.name;
		do {
			if(gInfo.turnTimeMS < 0) {
				log.info("Player " + current.name + " loses due to too much passed time.");
				current.over = true;
				postEndTurnToServer(current);
				break;
			}
			try{ 
				Thread.sleep(gInfo.turnTimeMS);
	    	} catch(InterruptedException ex){
	    	    Thread.currentThread().interrupt();
	    	}
			
			status = getEndTurnFromServer();
			
			if(status.pending) {
				log.warn("Player " + current.name + " skips turn.");
				continue;
			}
				
			int result;
			if(status.endTurn) {

				player  .setDir(isHost(current.name)? status.hostDir : status.guestDir);
				opponent.setDir(isHost(current.name)? status.guestDir: status.hostDir );
				current.endTurn = false;
				
		    	if(status.frogX >= 0) {
		    		log.info(Application.name + " adding a frog at " + status.frogX +", "+status.frogY);
		    		controller.frogs.add(new Position(status.frogX, status.frogY));
		    	}
				
			} else {
				if(isHost(current.name)) current.hostDir = player.getDir();
				else current.guestDir = player.getDir();
				current.endTurn = true;
				result = move(player);
			}
	    	
	    	log.debug("Player "+Application.name+" starts with current " + current + " and recieves status " + status + " from server.");
			
			//start of end turn -- first player registers for end of turn
			if(!status.endTurn) {
				log.debug("--- Start Turn ---");
				current.endTurn = true;			
				result = move(player);
			//end of turn -- second player makes turn, new turn starts
			} else {
				log.debug("--- End Turn ---");
				current.endTurn = false;		//end of end turn

				if(isHost(current.name)) current.guestDir = status.guestDir;
				else current.hostDir = status.hostDir;
				opponent.setDir(isHost(current.name)? status.guestDir: status.hostDir);
				move(opponent);
				result = move(player);
				
				Position frog = getFrogMabye();
				if(frog == null) {
					current.frogX = -1;
					current.frogY = -1;
				} else {
					current.frogX = frog.getX();
					current.frogY = frog.getY();
				}
				
			}
			
			current.over = true;
			switch(result) {
				case -1: log.info(current.name + "'s snake hits itself or collides with border"); break;
				case -2: log.info(current.name + "'s snake went outside border"); break;
				case -3: log.info(current.name + "'s snake hits opponent"); break;
				default: current.over = false;
			}
			
			postEndTurnToServer(current);
	    	
	    	view.repaint();
	    	gInfo.turnTimeMS -= gInfo.decreaseTimeMS;
		} while(!current.over);
		
		controller.view.setVisible(false);
		LobbyController.activate();
	}
	
	public boolean isHost(String name) {return name.equals(gInfo.hostName);}
	
	public static GameInfo getGameInfoFromServer() {
		String s = Application.serverAddress + "wait?name="+Application.name;
		return Application.restTemplate.getForObject(s, GameInfo.class);
	}
	
	public static void putDirToServer(int dir) {
		String s = Application.serverAddress + "aim?name="+Application.name+
				   "&dir="+(new Integer(dir)).toString();
		Application.restTemplate.getForObject(s, ResponseEntity.class);
	}
	
	public static Turn getEndTurnFromServer() {
		String s = Application.serverAddress + "endturn?name=" + Application.name;
		return Application.restTemplate.getForObject(s, Turn.class);
	}
	
	//if player passes end.over=true, that means the game ends, he loses.
	//if server sends information back with end.over=true, the opponent loses.
	public static Turn postEndTurnToServer(Turn end) {
		String s = Application.serverAddress + "endturn";
		log.debug("post from -->  " + end);
		if(end.name.equals(controller.gInfo.hostName)) end.hostDir = controller.player.getDir();
		else end.guestDir = controller.player.getDir();
		log.debug("post to   -->  " + end);
		return Application.restTemplate.postForObject(s, end, Turn.class);
	}
}
