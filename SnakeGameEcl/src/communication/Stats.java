package communication;

public class Stats {
	public String name;
	public int wins;
	public int losses;
	
	public String toString(){
		return "s,stts,"+name+","+wins+","+losses;
	}
	
	public static Stats fromString(String s) {
		Stats ps = new Stats();
		String[] stats = s.split(",");
		ps.name = stats[2];
		ps.wins = Integer.parseInt(stats[3]);
		ps.losses = Integer.parseInt(stats[4]);
		return ps;
	}
}
