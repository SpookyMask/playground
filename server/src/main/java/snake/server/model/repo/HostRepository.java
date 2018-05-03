package snake.server.model.repo;

import org.springframework.data.repository.CrudRepository;
import snake.server.model.comm.Host;

public interface HostRepository extends CrudRepository<Host, Long> {
	
}