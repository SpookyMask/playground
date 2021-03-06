package snake.server.model.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import snake.server.model.comm.User;

@Service
public interface UserRepository extends CrudRepository<User, Long> {
	public Optional<User> findByName(String name);
}