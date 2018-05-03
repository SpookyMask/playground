package snake.client.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.ResponseEntity;

import snake.client.Application;
import snake.client.model.comm.Host;
import snake.client.model.comm.Settings;
import snake.client.model.comm.Stats;
import snake.client.model.configs.ClientConfig;
import snake.client.model.game.Position;
import snake.client.model.game.Snake;
import snake.client.view.GameView;
import snake.client.view.LobbyView;

public class SingleplayerController extends Thread implements Controller {
	private static SingleplayerController controller;
	public GameView view;
	int slot = 0;
	Settings settings = null;
	public Snake player = null;
	public Snake opponent = null;
	public Set<Position> frogs = new HashSet<>();
	
	protected SingleplayerController() {
		view = GameView.getInstance();
		settings = new Settings();
	}
	
	public static SingleplayerController getInstance() {
		return controller == null? new SingleplayerController() : controller;
	}	
	
	public void onStart(Settings s) {
	    controller = new SingleplayerController();
	    controller.settings = s;
	    controller.slot = ThreadLocalRandom.current().nextInt(2);
	    controller.player = new Snake(slot);
	    controller.frogs = new HashSet<>();
	    controller.opponent = null;
		GameView.activate(controller.frogs, controller.player, controller.opponent, 
				          controller.settings.sizeN, controller.settings.sizeM);
		controller.start();
	}
	
	public void onDirUpdate(int newDir) {}
	
	public int move(Snake snake) {
		Position head = snake.stretch();
		if(head == null)
			return -1;  //snake hits self
		if( !settings.noBorder && head.outside() )
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
		if( ThreadLocalRandom.current().nextInt(0, settings.getFrogsDrop() ) != 0)
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
			try        
	    	{
				System.out.println(settings.turnTimeMS);
	    	    Thread.sleep(settings.turnTimeMS);
	    	} 
	    	catch(InterruptedException ex) 
	    	{
	    	    Thread.currentThread().interrupt();
	    	}
	    	view.repaint();
	    	settings.turnTimeMS -= settings.decreaseTimeMS;
		} while(turn() != -1);
		view.setVisible(false);
		MenuController.activate();
	}
}
