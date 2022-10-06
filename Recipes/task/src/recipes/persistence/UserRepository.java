package recipes.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.business.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    public User findUserByEmail(String email);
    public boolean existsByEmail(String email);
}
