package bll;

import dal.UserRepository;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return BCrypt.checkpw(password, user.getPassword());
        }
        return false;
    }

    public void addUser(String username, String password, String role) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(0, username, hashedPassword, role);
        userRepository.save(user);
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public List<String> getAllUsernames() {
        return userRepository.findAll().stream()
                .map(User::getUsername)
                .toList();
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    public void assignRoleToUser(String username, String newRole) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setRole(newRole);
            userRepository.updateUser(user);
        }
    }
}

