package snake.server.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import snake.client.model.configs.Constants;
import snake.server.model.comm.GameInfo;
import snake.server.model.comm.Turn;
import snake.server.model.comm.User;
import snake.server.model.repo.IDBService;

@Entity
public class Game{
	
	@Transient
	final static Logger log = Logger.getLogger(Game.class);
	
	@Autowired
	@Transient
	public IDBService dbService;
	
	@Id
	@GeneratedValue
	public int id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	public User host;

	@ManyToOne(cascade = CascadeType.ALL)
	public User guest;

	public boolean hostWon, guestWon;
	
	@Transient
	public GameInfo gInfo;
	
	@Transient
	private int hostDir = 0, guestDir = 2;
	
	@Transient
	private int turnNr = 0;
	
	@Transient
	private Turn current = null;
	
	@Transient
	private String checksum;
	
	@Transient
	String first;
	
	public Game(GameInfo gInfo) {
		laps.put(gInfo.hostName, System.currentTimeMillis() + Constants.gameStartDelay);
		laps.put(gInfo.guestName, System.currentTimeMillis() + Constants.gameStartDelay);
		lastLap = System.currentTimeMillis() + Constants.gameStartDelay;
		this.gInfo = gInfo;
	}
	
	public void setDir(String name, int dir){
		if(first != null) return;
		if(name.equals(gInfo.hostName)) hostDir = dir;
		else guestDir = dir;
	}
	
	@Transient
	Map<String, Long> laps = new HashMap<>();
	
	@Transient
	long lastLap;
	
	
	private boolean turnTimePassed(long lap) {
		float coeff = 0.1f;
		long deviation = (long)(coeff * lap);
	    
		//return lap >= gInfo.turnTimeMS - deviation && lap <= gInfo.decreaseTimeMS + deviation;
		return lap > gInfo.turnTimeMS / 2;
	}
	
	public Turn getTurn(Turn turn) {
	    
	    /* First player moved first, therefore the turn time has passed.
	     * Second player moved second therefore a small amount of time after the first player turn has passed.
	     * Other combinations are illegal.
	     */
	    
	    //long lap = System.currentTimeMillis() - laps.get(turn.name);
		long lap = System.currentTimeMillis() - lastLap;
	    
	    if((!turnTimePassed(lap) && first == null)||
	       ( turnTimePassed(lap) && first != null) ){
	    	log.warn("Skipping " + turn + " to adjust turn: turnTimePassed=" + turnTimePassed(lap) );
	    	laps.put(turn.name, System.currentTimeMillis());
	    	lastLap = System.currentTimeMillis();
	    	return null;
	    }
	    
	    laps.put(turn.name, System.currentTimeMillis());
	    lastLap = System.currentTimeMillis();
		
	    /*
	     * Turn starts -- init turn, first(player) and second(player)
	     * Turn ends   -- assert(first!=player), give turn to second
	     */
	    
	    if(current == null ) {
	    	
			current = new Turn(turn.name, turn.frogX, turn.frogY);
			current.turnNr = turnNr++;
			current.hostDir  = hostDir;
			current.guestDir = guestDir;

			first  = turn.name;
			
			log.info(current + " checksum passed...");
			checksum = turn.checksum;
			return current;
	    } else {
	    	if(!first.equals(turn.name)) {
	    		
				Turn entry = new Turn(current);
				entry.name = turn.name;
				current = null;
				first = null;
				gInfo.turnTimeMS -= gInfo.decreaseTimeMS;
				
				log.info("... " + entry + " checksum matched:" + ((checksum == null) ? "no_checksum": checksum.equals(turn.checksum)));
				checksum = turn.checksum;
				return entry;	    		
	    	}
	    }
		return null;
	}
	
}

