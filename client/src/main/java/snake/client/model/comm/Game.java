//package snake.client.model.comm
//public class Game extends Thread{
//	public long id;
//	Settings settings;
//	User host;
//	User guest;
//
//	@JsonIgnore
//	public boolean hostWon;
//	
//	@Transient
//	long startTime = 0;
//	
//	@Transient
//	private int turnTime;
//	
//	@Transient
//	private Message hostState;
//	
//	@Transient
//	private Message guestState;
//	
//	public Game(User host, User guest, Settings settings) {
//		super();
//		this.host = host;
//		this.guest = guest;
//		this.settings = settings;
//		turnTime = settings.turnTimeMS;
//		hostState = new Message(0,false);
//		guestState = new Message(2,false);
//	}
//	
//	public void scheduleStart() {
//		long now = System.currentTimeMillis();
//		startTime = now + Constants.gameStartDelay;
//	}
//	
//	public void turn() {
//	}
//
//	@Override
//	public void run() {
//		try {
//			Thread.sleep(turnTime);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		do {
//			if(hostState.endTurn && guestState.endTurn) {
//				hostState.startTurn = true;
//				guestState.startTurn = true;
//				break;
//			} else 
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//		} while(true);
//		
//		long now = System.currentTimeMillis();
//		startTime = now + turnTime;
//		turnTime -= settings.decreaseTimeMS;
//	}
//	
//	public Message getState(User player, Message message) {
//		Message m;
//		if(player == host) m = guestState;
//		else  m = hostState;
//		m.dir = message.dir;
//		return m;
//	}
//
//}
