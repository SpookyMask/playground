package snake.server.model;

public class Message {
	public int dir;
	public boolean endTurn;
	public boolean startTurn;
	public Message(int d, boolean e) {
		endTurn = e;
	}
}
