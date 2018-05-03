package snake.client.model.comm;

import snake.client.model.configs.Constants;

public class Settings {
	public long id;
	public int sizeN = Constants.sizeN, sizeM = Constants.sizeM;
	public boolean noBorder = Constants.noBorder;
	public int turnTimeMS = Constants.turnTimeMS;
	public int decreaseTimeMS = Constants.decreaseTimeMS;
}
