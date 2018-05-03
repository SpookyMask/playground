package snake.server.model.repo;

import org.springframework.data.repository.CrudRepository;
import snake.server.model.comm.Game;

public interface GameRepository extends CrudRepository<Game, Long> {
	
}
