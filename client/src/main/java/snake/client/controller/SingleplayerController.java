package snake.client.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import snake.client.Application;
import snake.client.model.comm.GameInfo;
import snake.client.model.game.Position;
import snake.client.model.game.Snake;
import snake.client.view.GameView;

public class SingleplayerController extends Thread implements Controller {
	private static SingleplayerController controller;
	protected GameView view;
	protected GameInfo gInfo;
	protected Snake player = null;
	protected Snake opponent = null;
	public Set<Position> frogs = new HashSet<>();
	protected int slot = 0;
	protected int frogsDrop = 0;
	
	protected SingleplayerController() {
		view = GameView.getInstance();
	}
	
	public static SingleplayerController getInstance() {
		return controller == null? new SingleplayerController() : controller;
	}
	
	protected void initController(GameInfo gInfo) {
	    controller = new SingleplayerController();
	}
	
	public void onStart(GameInfo gInfo) {
	    if(Application.name.equals(gInfo.hostName)) slot = gInfo.hostSlot;
	    else slot = (gInfo.hostSlot+1)%2;
	    frogsDrop = gInfo.sizeN * gInfo.sizeM * 3 / 40;
	    initController(gInfo);
	    controller.gInfo = gInfo;
	    controller.player = new Snake(slot);
	    controller.frogs = new HashSet<>();
		GameView.activate(controller.frogs, controller.player, controller.opponent, 
				          controller.gInfo.sizeN, controller.gInfo.sizeM);
		controller.start();
	}
	
	public void onDirUpdate(int newDir) {}
	
	public int move(Snake snake) {
		Position head = snake.stretch();
		if(head == null)
			return -1;  //snake hits self
		if( !gInfo.noBorder && head.outside() )
			return -1; //outside border
		if( opponent != null && opponent.contains(head) )
			return -1; //snake hits opponent
		
		if(frogs.contains(head))
			frogs.remove(head);
		else
			snake.shrink();
		
		return 0;
	}
	
	public int turn() {
		int over = move(player);
		
		//Game Turn
		addFrogMabye();
		
		return over;
	}

	public void addFrogMabye(){
		if( ThreadLocalRandom.current().nextInt(0, frogsDrop ) != 0)
			return;
		
		Position p;
		do{
			p = Position.random();
		} while( player.contains(p) ||
				 (opponent != null && opponent.contains(p)) ||
				 frogs.contains(p) );
		frogs.add(p);
	}
	
	@Override
	public void run() {		
		do {
			try {
	    	    Thread.sleep(gInfo.turnTimeMS);
	    	} catch(InterruptedException ex) {
	    	    Thread.currentThread().interrupt();
	    	}
	    	view.repaint();
	    	gInfo.turnTimeMS -= gInfo.decreaseTimeMS;
		} while(turn() != -1);
		view.setVisible(false);
		MenuController.activate();
	}
}
