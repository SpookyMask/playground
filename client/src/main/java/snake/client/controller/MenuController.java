package snake.client.controller;

import snake.client.view.MenuView;

public class MenuController implements Controller {
	private static MenuController controller = null;
	private static MenuView view = null;
	
	public static MenuController activate() {
		if(controller == null) controller = new MenuController(); 
		if(view == null) view = MenuView.activate();
		view.setVisible(true);
		return controller;
	}
	
	public static MenuController getInstance() {
		return controller == null? new MenuController() : controller;
	}
	
	public void onSingleplayerClick() {
		SettingsController.activate();	//Singleplayer
	}
	
	public void onConnectClick() {
	    LobbyController.activate();
	}

}
