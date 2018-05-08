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
	private Turn current;
	
	@OneToMany
	public Set<Turn> turns = new HashSet<>();
	
	public Game(GameInfo gInfo) {
		this.gInfo = gInfo;
		
		current = new Turn(true);
		current.hostDir = 0;
		current.guestDir = 2;
	}
	
	public void setDir(String name, int dir){
		if(hMoved || gMoved) return;
		if(name.equals(gInfo.hostName)) current.hostDir = dir;
		else current.guestDir = dir;
	}
	
	public Turn getTurn(Turn turn) {
		boolean isHost = turn.name.equals(gInfo.hostName);
	
		boolean moved = isHost? hMoved: gMoved;
		boolean otherMoved = isHost? gMoved: hMoved;
		
		if(moved) {
			
		} else if(!otherMoved) {

			current.turnNr++;
			current.name = turn.name;
			current.frogX = turn.frogX;
			current.frogY = turn.frogY;
			turns.add(current);
			
			if(isHost) hMoved = true;
			else gMoved = true;
			
		} else {
			if(isHost) gMoved = true;
			else hMoved = true;
		}

		log.info(current);
		return current;
	}

}
