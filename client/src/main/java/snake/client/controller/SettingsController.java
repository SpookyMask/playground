package snake.client.controller;

import snake.client.Application;
import snake.client.model.comm.GameInfo;
import snake.client.view.SettingsView;

public class SettingsController {
	private static SettingsController controller = null;
	private static SettingsView view = null;
	boolean multi = false;
	
	public static SettingsController activate() {
		if(controller == null) controller = new SettingsController(); 
		if(view == null) view = SettingsView.getInstance();
		view.setVisible(true);
		return controller;
	}
	
	public static SettingsController activate(boolean multiplayer) {
		if(controller == null) controller = new SettingsController(); 
		if(view == null) view = SettingsView.getInstance();
		view.setVisible(true);
		controller.multi = multiplayer;
		return controller;
	}
	
	public static SettingsController getInstance() {
		return controller == null? new SettingsController() : controller;
	}
	
	public void onStartClick(GameInfo gInfo) {
		if(multi) {
			postSettingsOnServer(gInfo);
			view.setVisible(false);
			MultiplayerController.waitForHost();
		} else {
			view.setVisible(false);
			SingleplayerController.onStart(gInfo);
		}
	}
	
	public static GameInfo postSettingsOnServer(GameInfo gInfo) {
		gInfo.hostName = Application.name;
		String s = Application.serverAddress + "host?name=" + gInfo.hostName;
		return Application.restTemplate.postForObject(s, gInfo, GameInfo.class);
	}
}
