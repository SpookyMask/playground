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
	private String checksum = "n/a";
	
	@Transient
	Long lastCall;
	
	@Transient
	Map<String, Long> laps = new HashMap<>();
	
	@Transient
	long previousGameTick = 0;
	
	@Transient
	private Turn current;

	@Transient
	private int moved = 0;
	@Transient
	private String checksOut = "";
	@Transient
	boolean gameStarted = false;
	
	public Game(GameInfo gInfo) {
		gameTick = 0;
		current = new Turn();
		current.hostDir = 0;
		current.guestDir = 2;
		current.name = "[Empty]";
		this.gInfo = gInfo;
	}
	
	public void setDir(String name, int dir){
//		if(onHold) return;
		if(name.equals(gInfo.hostName)) hostDir = dir;
		else guestDir = dir;
	}
	@Transient
	private long gameTick = System.currentTimeMillis();
	double tollerance = 0.1;
	@Transient
	private String second;
	@Transient
	private boolean onHold, fConsume, sConsume;
	@Transient
	long fAwaitedAt, sAwaitedAt;
	
	private int toInt(boolean b) {
		return b? 1: 0;
	}

	public Turn consumeTurn(Turn t) {
		current.sync = -1;
		current.refresh();
		current.name = t.name;
		
		String cS = "cs[i]";
		String type;

		if(current.isOver() || t.isOver()) {
			current.over();
		} else {
			current.refresh();
		}
			
			if(!fConsume && !sConsume) {
			current.turnNr++;
			current.frogX = t.frogX;
			current.frogY = t.frogY;
			current.hostDir = hostDir;
			current.guestDir = guestDir;
			current.checksum = t.checksum;
			second = t.name.equals(gInfo.hostName)? gInfo.guestName: gInfo.hostName;
			onHold = true;
			if(t.name.equals(second)) sConsume = true;
			else fConsume = true;
			type = "f";
		} else if(second != null && second.equals(t.name) && onHold) {
			onHold = false;
			if(t.name.equals(second)) fConsume = false;
			else sConsume = false;
			second = null;
			cS = t.checksum.equals(current.checksum)? "cs[o]": "cs[_]";
			type = "s";
		} else {
			current.idle();
			type = "w";
		}

		if(second != null &&  second.equals(t.name))
			current.sync = (System.currentTimeMillis() - gameTick);
	    
		log.info( type + "|"+(System.currentTimeMillis() - gameTick) + "\t\t" + toInt(fConsume) + "/"+toInt(sConsume)+ "/"+toInt(onHold)+"\t\t\t"+ current+"\t\t" + cS );
		gameTick = System.currentTimeMillis();
		
		return current;
	}
	
}

