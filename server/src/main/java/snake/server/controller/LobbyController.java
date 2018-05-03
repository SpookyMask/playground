package snake.server.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import snake.server.model.User;
import snake.server.model.comm.Host;
import snake.server.model.comm.Settings;
import snake.server.model.comm.Stats;
import snake.server.model.repo.IDBService;

@RestController
public class LobbyController {
	Map<Integer, Host> hosts = new HashMap<>();
	
	@Autowired
	public IDBService dbService;

    @GetMapping("stats")
    public Stats stats(@RequestParam(value="name") String name) {
    	User u = dbService.getUserByName(name);
    	if(u == null) {
    		u = new User(name);
    		dbService.updateUser(u);
    	}
    	return new Stats(u.name,u.wins,u.losses);
    }
    
    @GetMapping("hosts")
    public @ResponseBody List<Host> hosts() {
    	List<Host> objects = new ArrayList<Host>(hosts.values());
        return objects;
    }
    
	@PostMapping("host")
	public boolean host(@RequestParam(value="name") int name,
			            @RequestBody Settings settings) {
//	  if(hosts.containsKey(id)) return false;
//		  hosts.put(id, settings);
	
	  return true;
	}
    
//    @PostMapping("/host")
//    public boolean host(@RequestParam(value="id", required=true) int id,
//    		            @RequestBody Settings settings) {
//    	if(hosts.containsKey(id)) return false;
//    	hosts.put(id, settings);
//    	
//    	return true;
//    }
//    
//    @PostMapping("/join")
//    public Game join(@RequestParam(value="id", required=true) int id,
//    		         @RequestParam(value="host_id", required=true) int hostId) {
//    	if(hosts.containsKey(hostId)) {
//    		Game game = new Game(users.get(hostId), users.get(id), hosts.get(hostId));
//
//    	}
//    	return null;
//    }
//    
//    @GetMapping("/startin")
//    public Game wait(@RequestParam String name,
//                     @RequestParam int gameId) {
//    	Game g = null;
//    	for(Game gm : Application.playingGames)
//    		if(gameId == gm.id) g = gm;
//    	if(g == null) return null;
//    	else return g;
//    }

}
