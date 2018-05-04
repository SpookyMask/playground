package snake.client.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

import org.springframework.stereotype.Component;

import snake.client.model.game.Position;
import snake.client.model.game.Snake;

@Component
public class GameView extends JFrame implements KeyListener {
	private static GameView view = new GameView();
	public Snake player = null;
	public Snake opponent = null;
	public Set<Position> frogs = new HashSet<>();
	
	private GameView(){
		initGUI();
	}
	
	private void initGUI() {
		setBackground(Color.WHITE);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(200 , 450);
	    setLocation(200, 200);
	    setVisible(false);
        addKeyListener(this);	  
        setFocusable(true);
	    setFocusTraversalKeysEnabled(false);
	}
	
	public void paint(Graphics g) {
    	g.setColor(Color.WHITE);
        g.fillRect(0,0, Position.sizeN * 20 + 30, Position.sizeM * 20 + 30);
        
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
        if(e.getKeyCode()== KeyEvent.VK_RIGHT)
        	player.setDir(0);
        else if(e.getKeyCode()== KeyEvent.VK_LEFT)
        	player.setDir(2);
        else if(e.getKeyCode()== KeyEvent.VK_DOWN)
        	player.setDir(1);
        else if(e.getKeyCode()== KeyEvent.VK_UP)
        	player.setDir(3);
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}

	public static void activate(Set<Position> frogs, Snake player, Snake opponent, int n, int m) {
		if(view == null) view = new GameView();
		view.frogs = frogs;
		view.player = player;
		view.opponent = opponent;
		view.setSize(n*20+20 , m*20+40);
		view.setVisible(true);
	}
	
	public static GameView getInstance() {
		return view;
	}

}
