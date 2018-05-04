package snake.client.controller;

import java.util.HashSet;

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
		int slot = Application.name.equals(gInfo.hostName)? gInfo.hostSlot : (gInfo.hostSlot+1)%2;
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
	
	@Override
	public void run() {
		Turn status;
		System.out.println(Application.name + ": " + System.currentTimeMillis()%10000);
		do {
			if(gInfo.turnTimeMS < 0) break;
			try{
	    	    Thread.sleep(gInfo.turnTimeMS);
	    	} catch(InterruptedException ex){
	    	    Thread.currentThread().interrupt();
	    	}
	    	view.repaint();
	    	gInfo.turnTimeMS -= gInfo.decreaseTimeMS;
	    	
	    	status = postEndTurnToServer();
	    	if(status.over)
	    		break;
	    	else if(status.waiting) 
	    		continue;
	    	else if(status.penalty > 0) {
	    		gInfo.turnTimeMS -= status.penalty;
	    	}
	    	if(status.frogX >= 0) controller.frogs.add(new Position(status.frogX, status.frogY));
		} while(turn(status.dir) != -1);
		
		controller.view.setVisible(false);
		LobbyController.activate();
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
	
	public static Turn postEndTurnToServer() {
		String s = Application.serverAddress + "endturn";
		Turn endTurn = new Turn();
		endTurn.name = Application.name;
		endTurn.dir = controller.player.getDir();
		return Application.restTemplate.postForObject(s, endTurn, Turn.class);
	}
	
	public static void putOverToServer() {
		String s = Application.serverAddress + "over?name="+Application.name;
		Application.restTemplate.getForObject(s, ResponseEntity.class);
	}
}
