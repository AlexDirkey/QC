package bll;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dal.UserRepository;
import model.User;
import java.util.List;
import java.util.stream.Collectors;

public class AuthService {

    private final UserRepository userRepository;

    public AuthService() {
        this.userRepository = new UserRepository();
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) return null;

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        return result.verified ? user.getRole() : null;
    }

    public void addUser(String username, String password, String role) {
        String hash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        User user = new User(username, hash, role);
        userRepository.save(user);
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public List<String> getAllUsernames() {
        return userRepository.findAll().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    public boolean verifyPassword(String password, String hashed) {
        return BCrypt.verifyer().verify(password.toCharArray(), hashed).verified;
    }

    public String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}

