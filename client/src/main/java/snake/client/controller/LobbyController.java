package snake.client.controller;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.http.ResponseEntity;

import snake.client.Application;
import snake.client.controller.refresh.ReloadHostsTimerTask;
import snake.client.model.comm.GameInfo;
import snake.client.model.comm.User;
import snake.client.view.LobbyView;

public class LobbyController implements Controller {
	private static LobbyController controller = null;
	private static LobbyView view = null;

	public static LobbyController activate() {
		if(controller == null) controller = new LobbyController();
		if(view == null) view = LobbyView.getInstance();
	    
		if(view == null) 
			view = LobbyView.activate();
		view.setStats(getStatsFromServer());
		view.setHosts(getHostsFromServer());
		view.setVisible(true);

		// run this task as a background/daemon thread
		TimerTask timerTask = new ReloadHostsTimerTask();
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, 10*1000);
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
		GameInfo gInfo = getGameInfoFromServer();
		if(gInfo == null) return;
		view.setVisible(false);
		MultiplayerController.delayedStart(gInfo);
	}
	
	public static User getStatsFromServer() {
		String s = Application.serverAddress + "stats?name="+Application.name;
		User stats = Application.restTemplate.getForObject(s, User.class);
		Application.name = stats.name;
		return stats;
	}
	
	public static GameInfo[] getHostsFromServer() {
		String s = Application.serverAddress + "hosts";
		ResponseEntity<GameInfo[]> responseEntity = Application.restTemplate.getForEntity(s, GameInfo[].class);
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
