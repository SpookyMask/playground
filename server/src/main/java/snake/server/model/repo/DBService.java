package snake.server.model.repo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snake.server.model.User;

@Service
public class DBService implements IDBService {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	HostRepository hostRepo;
	
    public void updateUser(User user) {
    	userRepo.save(user);
    }
    
    public User getUserByName(String name) {
		Optional<User> u = userRepo.findByName(name);
		if(!u.isPresent()) return null;
    	return u.get();
    }

}
