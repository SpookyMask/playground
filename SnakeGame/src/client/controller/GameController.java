package client.controller;

import client.model.Status;
import client.view.SettingsView;
import client.view.GameView;
import communication.Settings;

public class GameController extends Controller {
	private static final GameController controller = new GameController();
	public static final SettingsView gameSettingsView = SettingsView.getInstance();
	static GameView gameView = null;
	
	boolean multiplayer = false;
	int slot = 0;
	Settings settings = null;
	
	public static Controller getInstance() {
		return controller;
	}

	@Override
	public void run() {
		switch(Status.state) {
		case ATSETTINGS:
			switch(Status.action) {
			case START:
				//Start the actual game :)
				settings = gameSettingsView.getSettings();
				gameSettingsView.setVisible(false);
				gameView = new GameView(settings, multiplayer);
				Status.action = Status.Actions.NA;
				Status.state = Status.States.PLAYING;
				break;
				default:
			}
			break;
		case PLAYING:
        	try        
        	{
        	    Thread.sleep(settings.turnTime);
        	} 
        	catch(InterruptedException ex) 
        	{
        	    Thread.currentThread().interrupt();
        	}
        	gameView.run();
        	gameView.repaint();
        	settings.turnTime *= settings.decreaseTime;
        	if(Status.state == Status.States.GAMEOVER) {
        		System.out.println("Game Over!");
        		break;
        	}
			break;
			default:
		}
			
	}
}
