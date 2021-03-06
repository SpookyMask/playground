package snake.client.controller;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;

import snake.client.Application;
import snake.client.model.comm.GameInfo;
import snake.client.model.comm.Turn;
import snake.client.model.comm.User;
import snake.client.model.configs.Constants;
import snake.client.model.game.Position;
import snake.client.model.game.Snake;
import snake.client.view.GameView;

public class MultiplayerController extends SingleplayerController {
	
	final public static Logger log = Logger.getLogger(MultiplayerController.class);

	private Snake host = null;
	private Snake guest = null;
	boolean over = false;
	int status = 0;
	
	
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
		if(gInfo.guestName.equals(Application.name))
			controller.view.setLocation(600, 200);
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
		GameView.activate(controller, controller.frogs, controller.host, controller.guest, 
				          controller.gInfo.sizeN, controller.gInfo.sizeM);
		
		controller.start();
	}
	
	public int turn(int direction) {
		int status = 0;
		guest.setDir(direction);
		
		if(slot == 0) {
			status = move(host);
			move(guest);
		} else {
			move(guest);
			status = move(host);
		}
		
		return status;
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
	
	private String serialize() {
		StringBuilder sb = new StringBuilder();
		sb.append(turnNr + " h[");
		String prefix = "";
		for(Position p : host.getList()) {
			sb.append(prefix);
			prefix = ",";
			sb.append(p);
		}
		sb.append("] g[");
		prefix = "";
		for(Position p : guest.getList()) {
			sb.append(prefix);
			prefix = ",";
			sb.append(p);
		}
		sb.append("] f[");
		prefix = "";
		for(Position p : frogs) {
			sb.append(prefix);
			prefix = ",";
			sb.append(p);
		}
		sb.append("] over=" + over + " status=" + status);
		return sb.toString();
	}
	
	// Players wait for the end of turn. When the first of them calls <<end of turn>>,
	// one moves and updates the game state. The second player calls <<end of turn>> and...
	
	//player receives info for last opponent turn if any.
	//if no last opponent turn: player turn and update game state.
	@Override
	public void run() {
		Turn turn;
		long sync = 0;
		
		log.info("[" + Application.name + "] starts game as " + (Application.name.equals(gInfo.hostName)? "host." : "guest." ));
		do {
			
			
			if(sync == -1) sync = 0;
			if(gInfo.turnTimeMS - sync <= 50) {
				gInfo.turnTimeMS = 500;
				sync = 0;
			}
			try{
				Thread.sleep(gInfo.turnTimeMS - sync);
	    	} catch(InterruptedException ex){
	    	    Thread.currentThread().interrupt();
	    	}
			gInfo.turnTimeMS -= gInfo.decreaseTimeMS;
			
			Position frog = getFrogMabye();
			turn = new Turn(Application.name, frog, serialize(), gInfo.turnTimeMS);
			turn.sync = gInfo.turnTimeMS;	//send supposed time for next turn
			turn = postEndTurnToServer(turn);

			sync = turn.sync;
			if(turn.isIdle()) {
				continue;
			} else if(turn.isOver()) {
				status = 1;
				break;
			}
			turnNr = turn.turnNr;
			
			host.setDir(turn.hostDir);
			guest.setDir(turn.guestDir);
			
			int h = move(host);
			int g = move(guest);
			
			if(turn.frogX != -1)
			    frogs.add(new Position(turn.frogX, turn.frogY));
			
		    status = Application.name.equals(gInfo.hostName)? h  : g ;
			over = h != 0 || g != 0;
			
			view.repaint();
		} while(!over);
			turn.over();
			postEndTurnToServer(turn);
		if(status < 0) {
			log.info("Player " + Application.name + " is leaving with status "+status+".");
			sendGameOverToServer(status);
		}
		
		view.dispose();
		LobbyController.activate();
	}
	
	
	
	public static GameInfo getGameInfoFromServer() {
		String s = Application.serverAddress + "wait?name="+Application.name;
		return Application.restTemplate.getForObject(s, GameInfo.class);
	}
	
	public static Turn getEndTurnFromServer() {
		String s = Application.serverAddress + "endturn?name=" + Application.name;
		return Application.restTemplate.getForObject(s, Turn.class);
	}
	
	public static Turn postEndTurnToServer(Turn end) {
		String s = Application.serverAddress + "endturn";
		return Application.restTemplate.postForObject(s, end, Turn.class);
	}
	
	public static User sendGameOverToServer(int status) {
		String s = Application.serverAddress + "over?name=" + Application.name + "&status=" + status;
		return Application.restTemplate.getForObject(s, User.class);
	}
    
	public static Turn sendDirToServer(int dir) {
		String s = Application.serverAddress + "move?name=" + Application.name +
				   "&dir=" + dir;
		return Application.restTemplate.getForObject(s, Turn.class);
	}

	@Override
	public void setDir(int d) {
		if(player.validate(d))
			sendDirToServer(d);
	}
	
}
