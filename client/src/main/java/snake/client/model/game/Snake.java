package snake.client.model.game;

import java.util.LinkedList;

public class Snake {
	public LinkedList<Position> body = new LinkedList<>();
    private int direction = 0;
    private int last_direction; //used to determine if head returns where it was
    public static boolean noBorder = true;
    static Position[] coord = new Position[]{ new Position(1,0),
            new Position(0,1),
            new Position(-1,0),
            new Position(0,-1) };
    
    public Snake(int player){
    	if(player == 0) {
	    	body.add(new Position(0,Position.sizeM-1));
	    	body.add(new Position(1,Position.sizeM-1));
	    	body.add(new Position(2,Position.sizeM-1));
	    	body.add(new Position(3,Position.sizeM-1));
	    	body.add(new Position(4,Position.sizeM-1));
	    	body.add(new Position(5,Position.sizeM-1));
	    	direction = 0;
    	} else {
	    	body.add(new Position(Position.sizeN-1,0));
	    	body.add(new Position(Position.sizeN-2,0));
	    	body.add(new Position(Position.sizeN-3,0));
	    	body.add(new Position(Position.sizeN-4,0));
	    	body.add(new Position(Position.sizeN-5,0));
	    	body.add(new Position(Position.sizeN-6,0));
	    	direction = 2;
    	}
    }
    
    public boolean contains(Position p) {
    	return body.contains(p);
    }
    
    public LinkedList<Position> getList(){
    	return new LinkedList<>(body);	//because of java.util.ConcurrentModificationException
    }
    
    //returns new head position
    public Position stretch() {
    	Position head_next;
    	head_next = body.peekLast().next(coord[direction]);
    	if(noBorder) head_next = head_next.normalize();
//    	else if(head_next.outside()) 
//    		return null;    //snake collides with border
    	
    	if( body.contains(head_next) )
    		return null;	//snake hits itself
    	body.addLast(head_next);
    	last_direction = direction;
    	return head_next;
    }
    
    public void shrink() {
        body.remove();
    }
    
    public boolean validate(int d) {
    	boolean moveBackwards = (d == 0 && last_direction == 2 ) ||
				        		(d == 1 && last_direction == 3 ) ||
				        		(d == 2 && last_direction == 0 ) ||
				        		(d == 3 && last_direction == 1 );
    	return !moveBackwards;
    }

    public void setDir(int d) {
    	if( (d == 0 && last_direction == 2 ) ||
    		(d == 1 && last_direction == 3 ) ||
    		(d == 2 && last_direction == 0 ) ||
    		(d == 3 && last_direction == 1 ) )
    		return;	//Can't move backwards!
    	direction = d;
    }
    
    public int getDir() {
    	return direction;
    }
    
}
