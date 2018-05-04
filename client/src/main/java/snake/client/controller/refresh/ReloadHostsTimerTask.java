package snake.client.controller.refresh;

import java.util.TimerTask;

import snake.client.controller.LobbyController;
import snake.client.model.comm.GameInfo;
import snake.client.view.LobbyView;

public class ReloadHostsTimerTask extends TimerTask {

	@Override
	public void run() {
		GameInfo[] hosts = LobbyController.getHostsFromServer();
		LobbyView.getInstance().setHosts(hosts);
	}

}
