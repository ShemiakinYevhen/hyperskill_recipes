package recipes.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.persistence.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserById(long id) {
        return userRepository.findById(id).orElse(new User());
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public boolean existsById(long id) {
        return userRepository.existsById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
