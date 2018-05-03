package snake.client.controller;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.http.ResponseEntity;

import snake.client.Application;
import snake.client.controller.refresh.ReloadHostsTimerTask;
import snake.client.model.comm.GameInfo;
import snake.client.model.comm.Host;
import snake.client.model.comm.Stats;
import snake.client.model.configs.ClientConfig;
import snake.client.view.LobbyView;

public class LobbyController implements Controller {
	private static LobbyController controller = null;
	private static LobbyView view = null;

	public static LobbyController activate() {
		if(controller == null) controller = new LobbyController();
		if(view == null) view = LobbyView.getInstance();
	    
		if(view == null) 
			view = LobbyView.activate(getStatsFromServer(), getHostsFromServer());
		else {
			view.setStats(getStatsFromServer());
			view.setHosts(getHostsFromServer());
		}		
		view.setVisible(true);

		// run this task as a background/daemon thread
		TimerTask timerTask = new ReloadHostsTimerTask();
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, 5*1000);
//        timer.cancel();
//        timer.purge();
		
		return controller;
	}
	
	public static LobbyController getInstance() {
		return controller == null? new LobbyController() : controller;
	}
	
	public void onHostClick() {
		view.setVisible(false);
		SettingsController.activate(true);
	}
	
	public void onJoinClick() {
		String host = view.getSelectedHost();
		GameInfo gInfo = getGameInfoFromServer();
		MultiplayerController.delayedStart(gInfo);
	}
	
	public static Stats getStatsFromServer() {
		String s = Application.serverAddress + "stats?name="+Application.name;
		Stats stats = Application.restTemplate.getForObject(s, Stats.class);
		Application.name = stats.name;
		return stats;
	}
	
	public static Host[] getHostsFromServer() {
		String s = Application.serverAddress + "hosts";
		ResponseEntity<Host[]> responseEntity = Application.restTemplate.getForEntity(s, Host[].class);
		return responseEntity.getBody();
	}
	
	public static GameInfo getGameInfoFromServer() {
		String host = view.getSelectedHost();
		if(host == null) return null;
		String s = Application.serverAddress + "join?name="+Application.name+
				   "&host="+host;
		return Application.restTemplate.getForObject(s, GameInfo.class);
	}
	

}
