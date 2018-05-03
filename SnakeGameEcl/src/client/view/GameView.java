package client.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;

import client.model.Status;
import client.model.game.Position;
import client.model.game.Snake;
import communication.Settings;

public class GameView extends JFrame implements KeyListener {
	//private Snake first = null;
	private int playerSlot = 0;
	private Snake player = null;
	private Snake opponent = null;
	private Set<Position> frogs = new HashSet<>();
	
	private boolean noborder = true;
	
	public GameView(Settings settings, boolean multiplayer){
		noborder = settings.noBorder;
		Position.sizeN = settings.sizeN;
		Position.sizeM = settings.sizeM;
	    playerSlot = settings.slot;
		player = new Snake(playerSlot);
		if(multiplayer)
			opponent = new Snake((playerSlot+1)%2);

		initGUI();
	}
	
	public void initGUI() {
		 setBackground(Color.WHITE);
	     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     setSize(400, 450);
	     setLocation(200, 200);
	     setVisible(true);
	     addKeyListener(this);
	     setFocusable(true);
	     setFocusTraversalKeysEnabled(false);
	}
	
	public int move(Snake snake) {
		Position head = snake.stretch();
		if(head == null)
			return -1;  //snake hits self
		if( !noborder && head.outside() )
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
	public void run() {
		int hit = move(player);
		if(hit == -1) Status.state = Status.States.GAMEOVER;
		
		//Game Turn
		if( ThreadLocalRandom.current().nextInt(0, 30 ) == 0)
		addFrog();
	}
	
	//Multiplayer
	public int run(int opp_direction) {
		int hit = 0;
		opponent.setDirection(opp_direction);
		
		if(playerSlot == 0) {
			hit = move(player);
			move(opponent);
		} else {
			move(opponent);
			hit = move(player);
		}
		
		return hit;
	}
	
	public void addFrog(){
		Position p;
		do{
			p = Position.random();
		} while( frogs.contains(p) ||
				 player.contains(p) ||
				 (opponent != null && opponent.contains(p)));
		frogs.add(p);
	}
	
	public void paint(Graphics g) {
    	g.setColor(Color.WHITE);
        g.fillRect(0,0, 430, 430);
        
	    g.setColor(Color.GREEN);
	    for(Position p : frogs)
	    	g.fillRect(p.getX()*20+5,p.getY()*20+30, 20, 20);
	    
	    g.setColor(Color.BLACK);
	    for(Position p : player.getList())
	    	g.fillRect(p.getX()*20+5,p.getY()*20+30, 20, 20);
	    
	    if(opponent == null) return;
	    for(Position p : opponent.getList())
	    	g.fillRect(p.getX()*20+5,p.getY()*20+30, 20, 20);
    }

	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println("keyPressed");
        if(e.getKeyCode()== KeyEvent.VK_RIGHT)
        	player.setDirection(0);
        else if(e.getKeyCode()== KeyEvent.VK_LEFT)
        	player.setDirection(2);
        else if(e.getKeyCode()== KeyEvent.VK_DOWN)
        	player.setDirection(1);
        else if(e.getKeyCode()== KeyEvent.VK_UP)
        	player.setDirection(3);
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	
	public int getSnakeDirection() {
		return player.getDirection();
	}

}
