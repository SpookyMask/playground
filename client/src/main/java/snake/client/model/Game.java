package snake.client.model;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import snake.client.model.comm.Settings;
import snake.client.model.game.Position;
import snake.client.model.game.Snake;
import snake.client.view.GameView;


public class Game extends Thread {
	public int id;
	int host, guest;
	Settings settings;
	public long startTime = 0;
	private long turnTime;
	int hostDir, guestDir;
	public boolean gameover = false;

	private GameView view;
	
	private int playerSlot = 0;
	public Snake player = null;
	public Snake opponent = null;
	public Set<Position> frogs = new HashSet<>();
	
	public Game(GameView view, Settings settings) {
		this.view = view;
		this.settings = settings;
	    playerSlot = ThreadLocalRandom.current().nextInt(2);
		player = new Snake(playerSlot);
		GameView.activate(this, settings.sizeN, settings.sizeM);
	}
	
	public Game(Settings settings) {
		this.view = view;
		this.settings = settings;
	    playerSlot = ThreadLocalRandom.current().nextInt(2);
		player = new Snake(playerSlot);
		GameView.activate(this, settings.sizeN, settings.sizeM);
	}
	
	public void setView(GameView v) {
		view = v;
	}
	
	public int move(Snake snake) {
		Position head = snake.stretch();
		if(head == null)
			return -1;  //snake hits self
		if( !settings.noBorder && head.outside() )
			return -1; //outside border
		if( opponent != null && snake == player && opponent.contains(head) )
			return -1; //snake hits opponent
		
		if(frogs.contains(head))
			frogs.remove(head);
		else
			snake.shrink();
		
		return 0;
	}
	
	//Singleplayer
	public int turn() {
		int over = move(player);
		//Game Turn
		if( ThreadLocalRandom.current().nextInt(0, 30 ) == 0)
			addFrog();
		return over;
	}
	
	//Multiplayer
	public int turn(int direction) {
		int over = 0;
		opponent.setDirection(direction);
		
		if(playerSlot == 0) {
			over = move(player);
			move(opponent);
		} else {
			move(opponent);
			over = move(player);
		}
		
		//Game Turn
		if( ThreadLocalRandom.current().nextInt(0, 30 ) == 0)
			addFrog();
		
		return over;
	}
	
	public void addFrog(){
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
	    	    Thread.sleep(settings.turnTimeMS);
	    	} 
	    	catch(InterruptedException ex) 
	    	{
	    	    Thread.currentThread().interrupt();
	    	}
	    	view.repaint();
	    	settings.turnTimeMS -= settings.decreaseTimeMS;
		} while(turn() != -1);
	}

}



//if(hit == -1) {
//	System.out.println("Game Over");
//	Quote quote = Application.restTemplate.getForObject(
//			"http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
//	Application.log.info(quote.toString());
//}
