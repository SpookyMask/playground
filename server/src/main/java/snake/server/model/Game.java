package snake.server.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import snake.server.Application;
import snake.server.model.comm.GameInfo;
import snake.server.model.comm.Turn;
import snake.server.model.comm.User;
import snake.server.model.repo.IDBService;

@Entity
public class Game{
	
	final static Logger log = Logger.getLogger(Game.class);
	
	@Autowired
	@Transient
	public IDBService dbService;
	
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
	private boolean hostMoved, guestMoved;
	
	@Transient
	private Turn current;
	
	@Transient
	private long penaltyStart;
	
	public Game(GameInfo gInfo) {
		super();
		this.gInfo = gInfo;
		current = new Turn("server", 0, 2, -1, 0, 0, false, false, false );
	}
	
	public Turn manageTurn(Turn playerTurn) {
		boolean moved = playerTurn.name.equals(gInfo.hostName)? hostMoved : guestMoved, 
		        otherMoved= playerTurn.name.equals(gInfo.hostName)? guestMoved : hostMoved;
		
		if(playerTurn.over) {
			log.info("Game Over");
			hostWon = playerTurn.name.equals(gInfo.hostName);
			guestWon = playerTurn.name.equals(gInfo.guestName);
			host = dbService.getUserByName(gInfo.hostName);
			guest = dbService.getUserByName(gInfo.guestName);
		}
		
		if(moved) {
			log.warn("Player " + playerTurn.name + "'s end turn is pending.");
			return current.pending();
		}
		
		if(!otherMoved) {
			current.endTurn = true;
			if(playerTurn.name.equals(gInfo.hostName)) hostMoved = true;
			else guestMoved = true;
		} else {
			hostMoved = false;
			guestMoved = false;
		}
		
		
		if(playerTurn.name.equals(gInfo.hostName))
			current.hostDir = playerTurn.hostDir;
		else
			current.guestDir = playerTurn.guestDir;
		
		return current;
	}
	
	public Turn getCurrentTurn() {
		return current;
	}

}
