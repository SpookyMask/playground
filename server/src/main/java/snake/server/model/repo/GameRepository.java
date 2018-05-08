package snake.server.model.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import snake.server.model.Game;
import snake.server.model.comm.User;

public interface GameRepository extends CrudRepository<Game, Long> {
	
}
