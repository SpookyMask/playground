package snake.client.model.game;

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
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
