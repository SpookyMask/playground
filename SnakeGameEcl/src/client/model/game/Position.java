package client.model.game;

import java.util.concurrent.ThreadLocalRandom;

public class Position extends Object{
	public static int sizeN = 20, sizeM = 20;
	private int x, y;
	
	public Position(int pos_x, int pos_y) {
		x = pos_x; y = pos_y;
	}
	public Position(Position pos) {
		x = pos.x; y = pos.y;
	}
	
	public Position plus(Position pos) {
		return new Position((x + pos.x + 20)%20, (y + pos.y + 20)%20);
	}
	
	public boolean outside() {
		return x < 0 || x >= sizeN || y < 0 || y >= sizeM;
	}
	
	//For Set
	@Override
	public boolean equals(Object obj){
		Position pos = (Position) obj;
		return pos.x == x && pos.y == y;
	}
	public boolean equals(int pos_x, int pos_y){
		return pos_x == x && pos_y == y;
	}    
	
	@Override
    public String toString() {
        return String.format("(",x,',',y,')');
    }
	
	public void setSize(int n, int m) {
		sizeN = n; sizeM = m;
	}
	
	public static Position random() {
	    int x = ThreadLocalRandom.current().nextInt(0, sizeN );
	    int y = ThreadLocalRandom.current().nextInt(0, sizeM );
	    return new Position(x, y);
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
}
