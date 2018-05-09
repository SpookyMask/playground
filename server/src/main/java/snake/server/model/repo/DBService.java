package snake.server.model.repo;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import snake.server.model.Game;
import snake.server.model.comm.Turn;
import snake.server.model.comm.User;
import snake.server.model.configs.Constants;

@Service
public class DBService implements IDBService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	TurnRepository turnRepo;

	@Autowired
	GameRepository gameRepo;
	
    public void updateUser(User user) {
    	userRepo.save(user);
    }
    
    public User getUserByName(String name) {
    	Optional<User> user = userRepo.findByName(name);
    	User u;
		if(user.isPresent()){
			u = user.get();
		} else {
			u = new User(name);
    		userRepo.save(u);
		}
    	return u;
    }
	
    public void updateGame(Game game) {
		updateUser(game.host);
		updateUser(game.guest);
    	gameRepo.save(game);
    }

}
