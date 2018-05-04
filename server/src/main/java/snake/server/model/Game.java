package snake.server.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import snake.server.model.comm.GameInfo;
import snake.server.model.comm.Turn;
import snake.server.model.comm.User;
import snake.server.model.configs.Constants;

@Entity
public class Game{
	
	@Id
	@GeneratedValue
	public int id;
	
	@ManyToOne
	public User host;
	
	@ManyToOne
	public User guest;

	public boolean hostWon, guestWon;
	
	@Transient
	public GameInfo gInfo;
	
	@Transient
	private Turn turn;
	
	@Transient
	private boolean hostEnded, guestEnded, gameSync;
	
	@Transient
	private long penaltyStart;
	
	@Transient
	private int hostDir, guestDir;
	
	public Game(GameInfo gInfo) {
		super();
		this.gInfo = gInfo;
	}
	
	public void updateDir(String name, int dir){
		if(hostEnded || guestEnded) return;
		if(name == gInfo.hostName) hostDir = dir;
		else this.guestDir = dir;
	}
	
	public Turn manageTurn(Turn playerTurn) {
		int oppDir;
		if(playerTurn.name.equals(gInfo.hostName)) {
			hostEnded = true;
			oppDir = guestDir;
		} else {
			guestEnded = true;
			oppDir = hostDir;
		}
		if(hostEnded && guestEnded) {	//second player ending turn
			hostEnded = false;
			guestEnded = false;
			long penalty = System.currentTimeMillis() - penaltyStart;
			return new Turn(oppDir,penalty);
		}
		else {		//first player ending turn
			gameSync = true;
			penaltyStart = System.currentTimeMillis();
			Turn sync = new Turn(oppDir,0);
			return sync;
		}
	}

}
