package snake.server.model.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import snake.server.model.comm.User;

public interface UserRepository extends CrudRepository<User, Long> {
	public Optional<User> findByName(String name);
}