package client.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import client.controller.ActionAdapter;
import client.model.Status;

public class MenuView extends JFrame {
	private static MenuView mainMenu;
	public  static JButton singleplayer;
	public  static JButton connect;
	
	private MenuView(){
		ActionAdapter buttonListener = null;
	    buttonListener = new ActionAdapter() {
	    	public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("single")) {
				Status.action = Status.Actions.SINGLE;
				System.out.println(Status.state + " " + Status.action);
			} else if(e.getActionCommand().equals("connect")){
				Status.action = Status.Actions.CONNECT;
				System.out.println(Status.state + " " + Status.action);
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
	
	public static MenuView getInstance() {
		if(mainMenu == null) mainMenu = new MenuView();
		return mainMenu;
	}
}
