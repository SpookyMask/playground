package snake.server.model.repo;

import org.springframework.data.repository.CrudRepository;

import snake.server.model.comm.Settings;

public interface SettingsRepository extends CrudRepository<Settings, Long> {
	
}