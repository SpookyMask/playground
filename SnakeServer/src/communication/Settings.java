package communication;

import java.util.concurrent.ThreadLocalRandom;

public class Settings {
	public int sizeN = 20, sizeM = 20;
	public boolean noBorder = true;
	public int turnTime = 1000;
	public double decreaseTime = 0.999999;
	
	public String name = "Player0";
	public int slot = ThreadLocalRandom.current().nextInt(0, 2 );
	
	public String toString(char platform) {
		return platform + ",stng," + sizeN + "," + sizeM + "," + noBorder + 
			   "," + turnTime + "," + decreaseTime + "," + name + "," + slot;
	}
	
	public static Settings fromString(String s) {
		Settings gs = new Settings();
		String[] stats = s.split(",");
		gs.sizeN = Integer.parseInt(stats[2]);
		gs.sizeM = Integer.parseInt(stats[3]);
		gs.noBorder = Boolean.parseBoolean(stats[4]);
		gs.turnTime = Integer.parseInt(stats[5]);
		gs.decreaseTime = Integer.parseInt(stats[6]);
		gs.name = stats[7];
		gs.slot = Integer.parseInt(stats[8]);
		return gs;
	}
}
