package snake.server.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import snake.client.model.configs.Constants;
import snake.server.Application;
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
	private boolean hMoved, gMoved;
	
	@Transient
	private int hostDir = 0, guestDir = 2;
	
	@Transient
	private int turnNr = 0;
	
	@Transient
	private Turn current = null;
	
	@Transient
	private String checksum;
	
	@Transient
	boolean moved = false;
	
	@OneToMany
	public Set<Turn> turns = new HashSet<>();
	
	public Game(GameInfo gInfo) {
		this.gInfo = gInfo;
	}
	
	public void setDir(String name, int dir){
		if(hMoved || gMoved) return;
		if(name.equals(gInfo.hostName)) hostDir = dir;
		else guestDir = dir;
	}
	
	public void setChecksum(String name, String cs){
		log.fatal(cs);
		if(current != null && moved)
			checksum = cs;
		else if(checksum == cs) {
			log.fatal("GAMES DIFFER!!!");
		}
	}
	
	public Turn getTurn(Turn turn) {
	    moved = (turn.name.equals(gInfo.hostName) && hMoved) ||
				        (turn.name.equals(gInfo.guestName) && gMoved);
		
		//log.debug("game turn: host(" + Constants.point[hostDir] + "/" + hMoved + "), guest(" + Constants.point[guestDir]  + "/" + gMoved + "), " + current);
		
		if(current == null) {
			
			current = new Turn(turn.name, turn.frogX, turn.frogY);
			current.turnNr = turnNr++;
			current.hostDir  = hostDir;
			current.guestDir = guestDir;
			
			if(turn.name.equals(gInfo.hostName)) hMoved = true;
			else gMoved = true;
			
			log.info(current + " *start*");
			return current;
			
		} else if(!moved) {
			
			turns.add(current);
			Turn entry = current;
			entry.name = turn.name;
			current = null;
			hMoved = false;
			gMoved = false;
			
			log.info(entry + " *end*");
			return entry;
			
		} else {

			log.warn(current + " waiting for end of turn");
			return null;
			
		}
		
	}
	
}

