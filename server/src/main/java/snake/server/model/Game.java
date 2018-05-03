package snake.server.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import snake.server.model.comm.GameInfo;
import snake.server.model.comm.Settings;
import snake.server.model.comm.Turn;
import snake.server.model.configs.Constants;

@Entity
public class Game{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int id;
	
	@JsonIgnore
	@ManyToOne
	public User host;
	
	@JsonIgnore
	@ManyToOne
	public User guest;
	
	public int sizeN = Constants.sizeN, sizeM = Constants.sizeM;
	
	public boolean noBorder = Constants.noBorder;
	
	public int turnTimeMS = Constants.turnTimeMS;
	
	public int decreaseTimeMS = Constants.decreaseTimeMS;

	@JsonIgnore
	public boolean hostWon, guestWon;
	
	@Transient
	private Turn turn;
	@Transient
	private GameInfo gInfo;
	@Transient
	private boolean hostEnded, guestEnded;
	
	public Game(GameInfo gInfo) {
		super();
		this.gInfo = gInfo;
	}
	
	public void updateDir(String name, int dir){
		if(hostEnded || guestEnded) return;
		if(name == gInfo.hostName) turn.hostDir = dir;
		else turn.dir = dir;
	}
	
	public boolean manageTurn(String name) {
		if(name == gInfo.hostName)
			hostEnded = true;
		else 
			guestEnded = true;
		if(hostEnded && guestEnded) {
			hostEnded = false;
			guestEnded = false;
			return true;
		}
		else return false;
	}

}
