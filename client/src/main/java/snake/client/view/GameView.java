package snake.client.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import org.springframework.stereotype.Component;

import snake.client.model.Game;
import snake.client.model.comm.Settings;
import snake.client.model.game.Position;

@Component
public class GameView extends JFrame implements KeyListener {
	private static GameView view = new GameView();
	private Game game;
	
	public GameView(){
		initGUI();
	}
	
	public void initGUI() {
		setBackground(Color.WHITE);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(200 , 450);
	    setLocation(200, 200);
	    setVisible(false);
        addKeyListener(this);	  
        setFocusable(true);
	    setFocusTraversalKeysEnabled(false);
	}
	
	public void newGame() {
		Game ng = new Game(this, new Settings());
		ng.run();
	}
	
	public void paint(Graphics g) {
    	g.setColor(Color.WHITE);
        g.fillRect(0,0, 430, 430);
        
	    g.setColor(Color.GREEN);
	    for(Position p : game.frogs)
	    	g.fillRect(p.getX()*20+5,p.getY()*20+30, 20, 20);
	    
	    g.setColor(Color.BLACK);
	    for(Position p : game.player.getList())
	    	g.fillRect(p.getX()*20+5,p.getY()*20+30, 20, 20);
	    
	    if(game.opponent == null) return;
	    for(Position p : game.opponent.getList())
	    	g.fillRect(p.getX()*20+5,p.getY()*20+30, 20, 20);
    }

	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println("keyPressed");
        if(e.getKeyCode()== KeyEvent.VK_RIGHT)
        	game.player.setDirection(0);
        else if(e.getKeyCode()== KeyEvent.VK_LEFT)
        	game.player.setDirection(2);
        else if(e.getKeyCode()== KeyEvent.VK_DOWN)
        	game.player.setDirection(1);
        else if(e.getKeyCode()== KeyEvent.VK_UP)
        	game.player.setDirection(3);
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}

	public static void activate(Game game, int n, int m) {
		if(view == null) view = new GameView();
		view.game = game;
		view.setSize(n*20+20 , m*20+40);
	}

	public static void activate() {
		if(view == null) view = new GameView();
		view.setVisible(true);
		view.game = new Game(new Settings());
		view.game.setView(view);
		view.game.start();
	}	
	
	public int run() {
		int i;
		do {
			try        
	    	{
	    	    Thread.sleep(1000);
	    	} 
	    	catch(InterruptedException ex) 
	    	{
	    	    Thread.currentThread().interrupt();
	    	}
	    	i = game.turn();
		} while(i != -1);
    	return -1;
	}

}
