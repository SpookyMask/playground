package snake.client.controller;

import snake.client.Application;
import snake.client.model.comm.Host;
import snake.client.model.comm.Settings;
import snake.client.view.SettingsView;

public class SettingsController implements Controller {
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
	
	public void onStartClick(Settings set) {
		if(multi) {
			Host host = postSettingsOnServer(set);
			view.setVisible(false);
			MultiplayerController.waitForHost();
		} else {
			view.setVisible(false);
			SingleplayerController.getInstance().onStart(set);
		}
	}
	
	public Host postSettingsOnServer(Settings set) {
		Host h = new Host(Application.name, set);
		String s = Application.serverAddress + "host";
		return Application.restTemplate.postForObject(s, h, Host.class);
	}
}
