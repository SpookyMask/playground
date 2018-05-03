package snake.server.model.comm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import snake.server.model.configs.Constants;

@Entity
public class Settings {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	
	public int sizeN = Constants.sizeN, sizeM = Constants.sizeM;
	
	public boolean noBorder = Constants.noBorder;
	
	public int turnTimeMS = Constants.turnTimeMS;
	
	public int decreaseTimeMS = Constants.decreaseTimeMS;
	
}
