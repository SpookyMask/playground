//			while ((response = is.readLine()) != null) {
//				System.out.println("READING "+response);
//		        if (response.equals("bye"))
//		            break;
//		    }

package client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

import client.model.Status;
import client.view.GameView;
import client.view.LobbyView;
import client.view.SettingsView;
import communication.Hosts;
import communication.Settings;
import communication.Stats;

public class ClientController extends Controller {
	private static final ClientController controller = new ClientController();
	public static SettingsView gameSettingsView;
	private static LobbyView view;
	private GameView gameView = null;
	
	private Status.Messages lastMessage = Status.Messages.NA;
	
    InetAddress address;
    Socket s1=null;
    String line=null;
    BufferedReader br=null;
    BufferedReader is=null;
    PrintWriter os=null;
    
    private String name = "Player" + ThreadLocalRandom.current().nextInt(0, 200);
    private Settings settings;
    
	public static Controller getInstance() {
		return controller;
	}
    
	private boolean connect() {
		try {
		    address=InetAddress.getLocalHost();
	        s1=new Socket(address, 4445); // You can use static final constant PORT_NUM
	        br= new BufferedReader(new InputStreamReader(System.in));
	        is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
	        os= new PrintWriter(s1.getOutputStream());
	        System.out.println(name + ": Connected to Server");
	        
	        //Authentication
	        sendRequest("c,auth," + name);
	        
	        return true;
	    }
	    catch (IOException e){
	    	if( lastMessage != Status.Messages.NOTCONNECTED ) {
	    		System.err.print("Client Connecting...\n");
	    		lastMessage = Status.Messages.NOTCONNECTED;
	    	}
	    }
		return false;
	}
	
	private void sendRequest(String msg){
            os.println(msg);
            os.flush();
            System.out.println(name + ": "+msg);
	}
	
	private String getResponse() throws IOException{
		String response = "";
		try{
			if(is.ready()) {
				response = is.readLine();
				
            System.out.println(name + " receives: "+response);
			}
		} catch(IOException e){
	    	System.err.println("Socket read Error");
	    	Status.state = Status.States.CONNECTING;
	        is.close();os.close();br.close();s1.close();
	        System.out.println("Connection Closed");
	    }
		return response;
	}

	@Override
	public void run() {
		switch(Status.state) {
		case CONNECTING: 
			boolean success = connect();	//authenticate
			if( success ) {
				view = LobbyView.getInstance();
				view.setVisible(true);
				Status.state = Status.States.ATLOBBY;
			}
		    break;
	    case ATLOBBY:
	    	String response=null;
	    	
	    	try {
	    		response = getResponse();

	    		if(response.contains("s,stts"))
	    			view.setStats(Stats.fromString(response));
	    		else if(response.contains("s,hsts"))
	    			view.setHosts(Hosts.fromString(response));
	    		
	    		if(Status.action == Status.Actions.HOST) {
	    			view.setVisible(false);
	    			SettingsView settingsView = SettingsView.getInstance();
	    			settingsView.setVisible(true);
	    			
	    			Status.state = Status.States.ATSETTINGS;
	    			Status.action = Status.Actions.NA;
	    		} else if(Status.action == Status.Actions.JOIN) {
	    			sendRequest("c,join," + view.getSelectedHost());
					Status.state = Status.States.WAITING;
					Status.action = Status.Actions.NA;
	    		}
	    	} catch(IOException e){
	    		System.err.println("Socket read Error");
	    	}
        	break;
		case ATSETTINGS:
			switch(Status.action) {
			case START:
				//Start the actual game :)
				SettingsView settingsView = SettingsView.getInstance();
				settings = settingsView.getSettings();
				settings.name = name;
				settingsView.setVisible(false);
				view = LobbyView.getInstance();
				view.setVisible(true);
				//gameView = new GameView(settings, true);
				sendRequest(settings.toString('c'));
				Status.state = Status.States.WAITING;
				Status.action = Status.Actions.NA;
				System.out.println("Client:" + name + " starts hosting.");
				break;
				default:
			}
			break;
		case WAITING:
			try {
				response = getResponse();
				
				if(response.contains("s,strt")) {
					//GAME STARTS
					view.setVisible(false);
					gameView = new GameView(Settings.fromString(response), true);
					Status.action = Status.Actions.NA;
					Status.state = Status.States.PLAYING;
				}
					
			} catch (IOException e) {
				System.err.println("Client:" + name + " hosting error.");
			}
			break;
		case PLAYING:
			try {
				response = getResponse();
				
				if(response.contains("s,turn")) {
					int status = gameView.run(Integer.parseInt(response.substring(7)));
					if( status == -1)
						sendRequest("c,over");
				} else if(response.contains("s,over")) {
					gameView.dispose();
					view.setVisible(true);
					Status.state = Status.States.ATLOBBY;
				}
				
				if(Status.action == Status.Actions.PRESS) {
					sendRequest("c,move" + gameView.getSnakeDirection());
					Status.action = Status.Actions.NA;
				}
					
					
			} catch (IOException e) {
				System.err.println("Client:" + name + " hosting error.");
			}
			
		default:
	    }
		
	}
	
//	public static void main(String[] args) {
//		Status.state = Status.States.CONNECTING;
//		while(true) {
//			controller.run();
//		}
//	}

}
