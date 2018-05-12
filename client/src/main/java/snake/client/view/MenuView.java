package snake.client.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.springframework.stereotype.Component;

import snake.client.Application;
import snake.client.controller.MenuController;

@Component
public class MenuView extends JFrame {
	private static MenuView menu;
	public  static JButton singleplayer;
	public  static JButton connect;
	
	private MenuView(){
		ActionAdapter buttonListener = null;
	    buttonListener = new ActionAdapter() {
	    	public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("single")) {
				// Start Single Player Game
				setVisible(false);
				MenuController.getInstance().onSingleplayerClick();
			} else if(e.getActionCommand().equals("connect")){
				// Connect to the lobby
				setVisible(false);
				Application.log.info(Application.name + " connecting...");
				MenuController.getInstance().onConnectClick();
				}
			}
	    };

		setTitle("SnakeGame>Menu");
	    JPanel mainPane = new JPanel(new BorderLayout());
	    singleplayer = new JButton("Singleplayer");
	    singleplayer.setActionCommand("single");
	    singleplayer.addActionListener(buttonListener);
	    connect = new JButton("Connect to server");
	    connect.setActionCommand("connect");
	    connect.addActionListener(buttonListener);
	    mainPane.add(singleplayer, BorderLayout.PAGE_START);
	    mainPane.add(connect, BorderLayout.CENTER);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setContentPane(mainPane);
	    setSize(400,400);
	    setLocation(200, 200);
	    setVisible(true);
	}
	
	public static MenuView activate() {
		if(menu == null) menu = new MenuView();
		menu.setVisible(true);
		return menu;
	}	

}
