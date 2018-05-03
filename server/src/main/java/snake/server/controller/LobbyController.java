package snake.server.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import snake.server.model.User;
import snake.server.model.comm.GameInfo;
import snake.server.model.comm.Stats;
import snake.server.model.configs.Constants;
import snake.server.model.repo.IDBService;

@RestController
public class LobbyController {
	public static Map<String, GameInfo> hosts = new HashMap<>();
	
	@Autowired
	public IDBService dbService;

    @GetMapping("stats")
    public Stats stats(@RequestParam(value="name") String name) {
    	User u;
    	if(name.equals("random")) {
    		String n;
    		do {
    			n = Constants.getRandomName();
    			u = dbService.getUserByName(n);
    		} while(u != null);
		    u = new User(n);
			dbService.updateUser(u);
    	} else {
    		u = dbService.getUserByName(name);
    		if(u == null) {
	    		u = new User(name);
	    		dbService.updateUser(u);
    		}
    	}
    	return new Stats(u.name,u.wins,u.losses);
    }
    
    @GetMapping("hosts")
    public @ResponseBody List<GameInfo> hosts() {
    	List<GameInfo> objects = new ArrayList<GameInfo>(hosts.values());
        return objects;
    }
    
	@PostMapping("host")
	public GameInfo host(@RequestBody GameInfo host) {
		if(hosts.containsKey(host.name)) return null;
		hosts.put(host.name, host);
		return host;
	}

}
