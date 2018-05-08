package snake.client.controller;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import snake.client.Application;
import snake.client.model.comm.GameInfo;
import snake.client.model.comm.Turn;
import snake.client.model.comm.User;
import snake.client.model.configs.Constants;
import snake.client.model.game.Position;
import snake.client.model.game.Snake;
import snake.client.view.GameView;

public class MultiplayerController extends SingleplayerController {

	protected Snake host = null;
	protected Snake guest = null;
	
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
		controller.view.setTitle("Game(" + gInfo.hostName + " vs. " + gInfo.guestName + ")");
		controller.view.setVisible(true);
		Position.sizeN = gInfo.sizeN;
		Position.sizeM = gInfo.sizeM;
	    Snake.noBorder = gInfo.noBorder;
	    controller.frogsDrop = gInfo.sizeN * gInfo.sizeM * 3 / 40;
	    controller.gInfo = gInfo;
	    controller.host = new Snake(0);
		controller.guest = new Snake(1);
		controller.player = isHost? controller.host: controller.guest;
	    controller.frogs = new HashSet<>();
		GameView.activate(controller.frogs, controller.host, controller.guest, 
				          controller.gInfo.sizeN, controller.gInfo.sizeM);
		
		log.info("[Client] " + Application.name + " is " + (Application.name.equals(gInfo.hostName)? "host" : "guest" ));
		controller.start();
	}
	
	
	@Override
	public void onDirUpdate(int newDir) {
		host.setDir(newDir);
	}
	
	public int turn(int direction) {
		int over = 0;
		guest.setDir(direction);
		
		if(slot == 0) {
			over = move(host);
			move(guest);
		} else {
			move(guest);
			over = move(host);
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
	
	@Override
	public Position getFrogMabye(){
		if( ThreadLocalRandom.current().nextInt(0, frogsDrop ) != 0)
			return null;
		
		Position p;
		do{
			p = Position.random();
		} while( host.contains(p) ||
				 (guest != null && guest.contains(p)) ||
				 frogs.contains(p) );
		return p;
	}
	
	// Players wait for the end of turn. When the first of them calls <<end of turn>>,
	// one moves and updates the game state. The second player calls <<end of turn>> and...
	
	//player receives info for last opponent turn if any.
	//if no last opponent turn: player turn and update game state.
	@Override
	public void run() {
		int lastTurnNr = 0;
		boolean lost = false;
		User me;
		
		log.info("[Client] " + Application.name + " starts game.");
		do {
			try{ 
				Thread.sleep(gInfo.turnTimeMS);
	    	} catch(InterruptedException ex){
	    	    Thread.currentThread().interrupt();
	    	}
			//gInfo.turnTimeMS -= gInfo.decreaseTimeMS;
			
			Turn turn = new Turn(Application.name);
			Position frog = getFrogMabye();
			if(frog == null) turn.frogX = -1;
			else{
				turn.frogX = frog.getX();
				turn.frogY = frog.getY();
			}
			
			turn = postEndTurnToServer(turn);

			if(turn.turnNr == lastTurnNr + 1) {
				
				host.setDir(turn.hostDir);
				guest.setDir(turn.guestDir);
				
				int h = move(host);
				int g = move(guest);
				
				if(turn.frogX != -1)
					frogs.add(new Position(turn.frogX, turn.frogY));
				
				turn.frogX = -1;
				
				int status = isHost(Application.name)? h  : g ;
				lost = status != 0;
				boolean otherLost = (isHost(Application.name)? g  : h) != 0 ;
				
				if(lost) {
					log.info("[Client] " + Application.name + " loses game.");
					me = sendGameOverToServer(status);
					break;
				} else if(otherLost) {
					log.info("[Client] " + Application.name + ":  Unknown connection error.");
					break;
				}
				
				lastTurnNr = turn.turnNr;
				
			} else if(turn.turnNr == lastTurnNr) {
				log.info("[Client] " + Application.name + " waiting for turn "+ (lastTurnNr+1) +" .");
			    continue;
			} else {
				log.error("[Client]" + Application.name + "  turn nr mismatch.");
				break;
			}
			
			view.repaint();
		} while(!lost);
	}
	
	public boolean isHost(String name) {return name.equals(gInfo.hostName);}
	
	public static GameInfo getGameInfoFromServer() {
		String s = Application.serverAddress + "wait?name="+Application.name;
		return Application.restTemplate.getForObject(s, GameInfo.class);
	}
	
	public static Turn getEndTurnFromServer() {
		String s = Application.serverAddress + "endturn?name=" + Application.name;
		return Application.restTemplate.getForObject(s, Turn.class);
	}
	
	//if player passes end.over=true, that means the game ends, he loses.
	//if server sends information back with end.over=true, the opponent loses.
	public static Turn postEndTurnToServer(Turn end) {
		String s = Application.serverAddress + "endturn";
		//log.debug("post from -->  " + end);
//		if(end.name.equals(controller.gInfo.hostName)) end.hostDir = controller.host.getDir();
//		else end.guestDir = controller.host.getDir();
		//log.debug("post to   -->  " + end);
		return Application.restTemplate.postForObject(s, end, Turn.class);
	}
	
	public static User sendGameOverToServer(int status) {
		String s = Application.serverAddress + "over?name=" + Application.name + "&status=" + status;
		return Application.restTemplate.getForObject(s, User.class);
	}
}
