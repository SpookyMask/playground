package communication;

import java.util.Arrays;


public class Hosts {
	public String[][] data = new String[][] {{null}};
	
	public String toString(){
		StringBuilder sb = new StringBuilder(1000);
		sb.append("s,hsts,");
		for(int i = 0; i < data.length - 1; i++) {
			sb.append(data[i][0]);
			sb.append(",");
		}
		sb.append(data[data.length-1][0]);
		return sb.toString();
	}
	
	public static Hosts fromString(String s) {
		Hosts h = new Hosts();
		String[] stats = s.split(",");
		String[] rows = Arrays.copyOfRange(stats, 1, stats.length);
		h.data = new String[rows.length-2][1];
		for(int i=2; i < rows.length-1; i++)
			h.data[i-2][0] = rows[i];
		return h;
	}
}
