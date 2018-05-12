package snake.client.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;

import snake.client.Application;
import snake.client.model.comm.GameInfo;
import snake.client.model.game.Position;
import snake.client.model.game.Snake;
import snake.client.view.GameView;

public class SingleplayerController extends Thread implements IGameController {
	
	final public static Logger log = Logger.getLogger(SingleplayerController.class);
	
	private static SingleplayerController controller;
	protected GameView view;
	protected GameInfo gInfo;
	protected Snake player = null;
	public Set<Position> frogs = null;
	protected int slot = 0;
	protected int frogsDrop = 0;
	protected int turnNr = 0;
	
	protected SingleplayerController() {
		view = GameView.getInstance();
	}
	
	public static SingleplayerController getInstance() {
		return controller == null? new SingleplayerController() : controller;
	}
	
	protected static SingleplayerController initController(GameInfo gInfo) {
	    return new SingleplayerController();
	}
	
	
	
	public static void onStart(GameInfo gInfo) {
		controller = new SingleplayerController();
		int slot = Application.name.equals(gInfo.hostName)? gInfo.hostSlot : (gInfo.hostSlot+1)%2;
		controller.view.setTitle("Snake Game - Singleplayer");
		controller.view.setVisible(true);
		Position.sizeN = gInfo.sizeN;
		Position.sizeM = gInfo.sizeM;
	    Snake.noBorder = gInfo.noBorder;
	    if(Application.name.equals(gInfo.hostName)) controller.slot = gInfo.hostSlot;
	    else controller.slot = slot;
	    controller.frogsDrop = gInfo.sizeN * gInfo.sizeM * 3 / 40;
	    controller.gInfo = gInfo;
	    controller.player = new Snake(controller.slot);
	    controller.frogs = new HashSet<>();
		GameView.activate(controller, controller.frogs, controller.player, null, 
				          controller.gInfo.sizeN, controller.gInfo.sizeM);
		controller.start();
	}
	
	public int move(Snake snake) {
		Position head = snake.stretch();
//		log.debug(turnNr + ". Snake stretches " + Constants.point[snake.getDir()] + "->" + head);
		if(head == null)
			return -1;  //snake hits itself
		if( !gInfo.noBorder && head.outside() )
			return -2; //outside border
		if( player != snake && player.contains(head) )
			return -3; //snake hits opponent
		
		if(frogs.contains(head)) {
			frogs.remove(head);
			log.debug("Snake " + snake + " eats a frog. ");
		}
		else
			snake.shrink();
		
		return 0;
	}
	
	public int turn() {
		int over = move(player);
		
		//Game Turn
		Position frog = getFrogMabye();
		if(frog != null) frogs.add(frog);
		
		return over;
	}

	public Position getFrogMabye(){
		if( ThreadLocalRandom.current().nextInt(0, frogsDrop ) != 0)
			return null;
		
		Position p;
		do{
			p = Position.random();
		} while( player.contains(p) || frogs.contains(p) );
		return p;
	}
	
	@Override
	public void run() {		
		do {
			try {
	    	    Thread.sleep(gInfo.turnTimeMS);
	    	} catch(InterruptedException ex) {
	    	    Thread.currentThread().interrupt();
	    	}
			view.frogs = new HashSet<>(frogs); //because of java.util.ConcurrentModificationException
	    	view.repaint();
	    	gInfo.turnTimeMS -= gInfo.decreaseTimeMS;
		} while(turn() != -1);
		view.setVisible(false);
		MenuController.activate();
	}

	@Override
	public void setDir(int d) {
		player.setDir(d);
	}
}
