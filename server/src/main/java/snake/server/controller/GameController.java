package snake.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import snake.server.model.Game;
import snake.server.model.comm.GameInfo;
import snake.server.model.comm.Turn;
import snake.server.model.repo.IDBService;

@RestController
public class GameController {
	
	@Autowired
	Logger log;
	
	@Autowired
	public IDBService dbService;

	Map<String, Game> runningGames = new HashMap<>();
	
    @GetMapping("join")
    public GameInfo join(@RequestParam(value="name") String name,
  		                 @RequestParam(value="host") String hostname) {
    	if(!LobbyController.hosts.containsKey(hostname)) {
    		log.debug("User " + name + " doesn't find host " + hostname);
    		return null;
    	}
    	GameInfo gInfo = LobbyController.hosts.get(hostname);
	    gInfo.guestName = name;
	    gInfo.setTimeStamp();
	    Game game = new Game(gInfo);
	    runningGames.put(name, game);
	    runningGames.put(hostname, game);
	    LobbyController.hosts.remove(hostname);
	    log.info("Game(" + hostname + " vs. " + name + ") will start in " + 
	             gInfo.startsIn/1000 + " " + gInfo.startsIn%1000 );
  	    return gInfo;
    }
    
    @GetMapping("wait")
    public GameInfo wait(@RequestParam(value="name") String name) {
    	if(!runningGames.containsKey(name)) {
    		System.out.println("Host " + name + " waits again...");
    		return null;
    	}
    	Game game = runningGames.get(name);
    	game.gInfo.updateStartIn();
    	log.info("Game(" + game.gInfo.hostName + " vs. " + game.gInfo.guestName + "): " + 
    	               "host game.gInfo.hostName is notified, game starts in " +
    	               game.gInfo.startsIn/1000 + " " + game.gInfo.startsIn%1000);
    	return game.gInfo;
    }
	
	@PostMapping("endturn")
	public Turn endturn(@RequestBody Turn turn) {
		Game game = runningGames.get(turn.name);

		log.info("Player " + turn.name + " ends " + turn);
		if(turn.over) {
			game.hostWon = game.gInfo.guestName.equals(turn.name);
			runningGames.remove(game.gInfo.hostName);
			runningGames.remove(game.gInfo.guestName);
			log.info("Game(" + game.gInfo.hostName + " vs. " + game.gInfo.guestName + ") ended, winner is " + 
			         ( game.hostWon? " host " + game.gInfo.hostName: " guest " + game.gInfo.guestName ));
		}
		
		return game.manageTurn(turn);
	}
	
    @GetMapping("endturn")
    public Turn endturn(@RequestParam(value="name") String name) {
    	Game game = runningGames.get(name);
    	return game.getCurrentTurn();
    }	

}
