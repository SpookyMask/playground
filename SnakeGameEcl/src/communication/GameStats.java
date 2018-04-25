package communication;

import java.time.LocalDateTime;

public class GameStats {
	public int winner;
	public String time;
	
	public String toString(){
		return "1,"+winner+","+time;
	}
	
	public static GameStats fromString(String s) {
		GameStats gs = new GameStats();
		String[] stats = s.split(",");
		gs.winner = Integer.parseInt(stats[1]);
		LocalDateTime currentTime = LocalDateTime.now();
		gs.time = currentTime.toString();
		return gs;
	}
}
