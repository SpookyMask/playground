package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import communication.Settings;
import communication.Stats;

public class ServerThread extends Thread{
	public enum States{AUTHENTICATING, ATLOBBY, HOSTING};
	public States state = States.AUTHENTICATING;

    String request=null;
    BufferedReader  is = null;
    PrintWriter os=null;
    Socket s=null;

    LinkedList<ServerThread> users;
    LinkedList<ServerThread> hosts;
	
	public Stats user = new Stats();
	
	//Game variables
	private Settings settings = null;
	private ServerThread opponent = null;
	private boolean runningaGame = false;
	private int direction = 0;
	private long lastTime = 0;
    
    public ServerThread(Socket s, LinkedList<ServerThread> u, LinkedList<ServerThread> h ){
        this.s=s;
        users = u;
        hosts = h;
    }
    
    public String hostsStringify() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("s,hsts");
    	for(ServerThread host : hosts) {
    		sb.append(",");
    		sb.append(host.user.name);
    	}
    	return sb.toString();
    }
    
    private void sendResponse( String s ) {
    	os.println(s + "\n");
    	os.flush();
    	System.out.println("Server to " + user.name + ": " + s);
    }
    
    public void startGame(ServerThread op, Settings gameSettings) {
    	opponent = op;
    	sendResponse(gameSettings.toString('s'));
    }
    
    public void endGame(boolean won) {
    	sendResponse("s,over");
    	if( won ) ++user.wins;
    	else  ++user.losses;
    	sendResponse(user.toString());
    }
    
    public void run() {
        try{
        	
            is= new BufferedReader(new InputStreamReader(s.getInputStream()));
            os=new PrintWriter(s.getOutputStream());

        }catch(IOException e){
            System.err.println(user + ": IO error in server thread");
        }
        
        
        //GAME TURN
        if(runningaGame) {
	        long currentTime = System.currentTimeMillis();
	        if(currentTime - lastTime > settings.turnTime) {
	        	sendResponse("s,move");
	        	opponent.sendResponse("s,move");
	        	lastTime = currentTime;
	        	settings.turnTime *= settings.decreaseTime;
	        }
        }
        
        try {
        	
        	request=is.readLine();
        	System.out.println("Server receives " + request);
        	
        	if(request.contains("c,move")) {
            	if(runningaGame)
            		direction = Integer.parseInt(request.substring(7));
            	else
        			opponent.setDirection( Integer.parseInt(request.substring(7)));
        	} else if(request.contains("c,over")) {
            		endGame(!runningaGame);
            		opponent.endGame(runningaGame);
        	} else {
	        	if(request.contains("c,auth")) {
	        		user.name = request.substring(7);
	        		user.wins = ThreadLocalRandom.current().nextInt(0, 100 );
	        		user.losses = ThreadLocalRandom.current().nextInt(0, 100 );
	        		sendResponse(user.toString());
	        		if(hosts.size() > 0)
	        			sendResponse(hostsStringify());
	        	} else if(request.contains("c,stng")) {
	        		settings = Settings.fromString(request);
	        		hosts.push(this);
	        		for(ServerThread u: users)
	        			u.sendResponse(hostsStringify());
	        	} else if(request.contains("c,join")) {
	        		//find host by name
	        		String hostName = request.substring(7);
	        		ServerThread host = null;
	        		for(ServerThread h: hosts) 
	        			if(hostName == h.user.name) {
	        				host = h;
	        				break;
	        			}
	        		if(host != null) {
		        		startGame(this, settings);
		        		settings.slot = (settings.slot+1)%2;
		        		host.startGame(host, settings);
		        		host.runningaGame = true;
	        		}
	        	}
        	}
        			
        	
        } catch (IOException e) {
      
            System.err.println("Server: IO Error " + user.name + " "+request+" terminated abruptly");
            
            try{
                System.out.println("Server: Connection Closing..");
                if (is!=null){
                    is.close(); 
                    System.out.println(" Socket Input Stream Closed");
                }

                if(os!=null){
                    os.close();
                    System.out.println("Socket Out Closed");
                }
                if (s!=null){
                s.close();
                System.out.println("Socket Closed");
                }

                }
            catch(IOException ie){
                System.err.println("Socket Close Error");
            }
        
        } catch(NullPointerException e){
        	
            request=this.getName(); //reused String line for getting thread name
            System.out.println("Server: Client "+user.name+" Closed");
        }
    }
    
    public void setDirection( int d ) {
    	direction = d;
    }
}