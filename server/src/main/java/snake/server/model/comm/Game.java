package snake.server.model.comm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import snake.server.model.Message;
import snake.server.model.User;
import snake.server.model.configs.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Game extends Thread{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	
	@OneToOne
	Settings settings;
	
	@JsonIgnore
	@ManyToOne
	User host;
	
	@JsonIgnore
	@ManyToOne
	User guest;

	@JsonIgnore
	public boolean hostWon;
	
	@Transient
	long startTime = 0;
	
	@Transient
	private int turnTime;
	
	@Transient
	private Message hostState;
	
	@Transient
	private Message guestState;
	
	public Game(User host, User guest, Settings settings) {
		super();
		this.host = host;
		this.guest = guest;
		this.settings = settings;
		turnTime = settings.turnTimeMS;
		hostState = new Message(0,false);
		guestState = new Message(2,false);
	}
	
	public void scheduleStart() {
		long now = System.currentTimeMillis();
		startTime = now + Constants.gameStartDelay;
	}
	
	public void turn() {
	}

	@Override
	public void run() {
		try {
			Thread.sleep(turnTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		do {
			if(hostState.endTurn && guestState.endTurn) {
				hostState.startTurn = true;
				guestState.startTurn = true;
				break;
			} else 
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		} while(true);
		
		long now = System.currentTimeMillis();
		startTime = now + turnTime;
		turnTime -= settings.decreaseTimeMS;
	}
	
	public Message getState(User player, Message message) {
		Message m;
		if(player == host) m = guestState;
		else  m = hostState;
		m.dir = message.dir;
		return m;
	}

}
