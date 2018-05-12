package snake.server.model.repo;

import org.springframework.data.repository.CrudRepository;

import snake.server.model.comm.Turn;

public interface TurnRepository extends CrudRepository<Turn, Long> {
	
}
