package snake.client.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import snake.client.Application;
import snake.client.model.comm.Host;
import snake.client.model.comm.HostsView;
import snake.client.model.comm.Settings;
import snake.client.model.comm.Stats;
import snake.client.model.configs.ClientConfig;

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
				GameView.activate();
			} else if(e.getActionCommand().equals("connect")){
				// Connect to the lobby
				setVisible(false);
				System.out.println("Connecting...");
				String s = Application.serverAddress + "stats?name="+ClientConfig.getProperty("player_name");
				Stats stats = Application.restTemplate.getForObject(s, Stats.class);
//				HostsView hosts = Application.restTemplate.getForObject(
//						Application.serverAddress + "/hosts", HostsView.class);
//				HostsView hosts = new HostsView(new String[][] {});
//				List<Host> hosts = Application.restTemplate.getForObject(
//						Application.serverAddress + "/hosts", Host.class);
				s = Application.serverAddress + "hosts";
				ResponseEntity<Host[]> responseEntity = Application.restTemplate.getForEntity(s, Host[].class);
				System.out.println(responseEntity);
				Application.id = stats.id;
			    LobbyView.activate(stats, responseEntity.getBody());
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
