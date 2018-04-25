package client.controller;

import client.model.Status;
import client.view.MenuView;

public class MenuController extends Controller {
	private static MenuController controller = null;
	private static MenuView view = null;
	
	public static MenuController getInstance() {
		if(controller == null) controller = new MenuController(); 
		if(view == null) view = MenuView.getInstance();
		//view.setVisible(true);
		return controller;
	}

	@Override
	public void run() {
		switch(Status.state) {
		case ATMENU: 
			switch(Status.action) {
			case SINGLE:
				view.setVisible(false);
				System.out.println(controller);
				Status.conrState = Status.ControllerStates.TOGAME;
				break;
			case CONNECT:
				view.setVisible(false);
				view.dispose();
				Status.state = Status.States.CONNECTING;
				Status.conrState = Status.ControllerStates.TOCLIENT;
			}
			break;
			default:
		}
	}

}
