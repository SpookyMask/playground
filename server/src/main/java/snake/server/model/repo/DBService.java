package snake.server.model.repo;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import snake.server.model.comm.User;
import snake.server.model.configs.Constants;

@Service
public class DBService implements IDBService {

	@Autowired
	UserRepository userRepo;
	
    public void updateUser(User user) {
    	userRepo.save(user);
    }
    
    public User getUserByName(String name) {
    	Optional<User> u = userRepo.findByName(name);
		if(!u.isPresent()) return null;
    	return u.get();
    }

}
