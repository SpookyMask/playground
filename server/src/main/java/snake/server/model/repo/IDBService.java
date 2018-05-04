package snake.server.model.repo;

import snake.server.model.comm.User;

public interface IDBService {
    User getUserByName(String name);
    void updateUser(User user);
}
