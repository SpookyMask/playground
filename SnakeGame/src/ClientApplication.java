import client.controller.ClientController;
import client.controller.Controller;
import client.controller.GameController;
import client.controller.MenuController;
import client.model.Status;
import client.model.Status.States;

public class ClientApplication {
	
	public static void main(String[] args) {
		Controller controller = null;
		while(true) {
			switch(Status.conrState) {
			case TOMENU: 
				controller = MenuController.getInstance();
				Status.state = States.ATMENU;
				Status.conrState = Status.ControllerStates.NA;
				break;
			case TOGAME:
				controller = GameController.getInstance();
				Status.state = States.ATSETTINGS;
				Status.conrState = Status.ControllerStates.NA;
				break;
			case TOCLIENT:
				controller = ClientController.getInstance();
				Status.state = States.CONNECTING;
				Status.conrState = Status.ControllerStates.NA;
				break;
			default:
			}
			
			controller.run();
		}
	}

}
