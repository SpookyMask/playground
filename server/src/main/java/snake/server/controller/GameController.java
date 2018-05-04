package snake.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public IDBService dbService;

	Map<String, Game> runningGames = new HashMap<>();
	
    @GetMapping("join")
    public GameInfo join(@RequestParam(value="name") String name,
  		                 @RequestParam(value="host") String hostname) {
    	GameInfo gInfo = null;
    	if(LobbyController.hosts.containsKey(hostname)) {
    		gInfo = LobbyController.hosts.get(hostname);
  		    gInfo.guestName = name;
  		    gInfo.setTimeStamp();
  		    Game game = new Game(gInfo);
  		    runningGames.put(name, game);
  		    runningGames.put(hostname, game);
  		    LobbyController.hosts.remove(hostname);
        }
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
    	return game.gInfo;
    }
	
	@PostMapping("endturn")
	public Turn endturn(@RequestBody Turn turn) {
		Game game = runningGames.get(turn.name);
		turn = game.manageTurn(turn);
		return turn;
	}
	
	@GetMapping("over")
	public void over(@RequestParam(value="name") String name) {
		Game game = runningGames.get(name);
		runningGames.remove(game.gInfo.hostName);
		runningGames.remove(game.gInfo.guestName);
		
		//dbService.
		System.out.println("SERVER GAMEOVER!");
	}
	

}
