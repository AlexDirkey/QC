package bll;

import dal.UserRepository;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

// Authservice håndterer login, oprettelse og administration er brugere
public class AuthService {
    private final UserRepository userRepository;

    //Contructor, der opretter et UserRepository
    public AuthService() {
        this(new UserRepository());
    }

    // Construktor med injection. Brugt til tests
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Forsøger at logge en bruger ind
    public User authenticate(String username, String password) throws AuthException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AuthException("Bruger ikke oprettet – kontakt administrator.");
        }
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new AuthException("Forkert brugernavn eller password – kontakt administrator.");
        }
        return user;
    }

    // Opretter en ny bruger med et hashet password
    public void addUser(String username, String password, String role) {
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(0, username, hash, role);
        userRepository.save(user);
    }

    //Checker, om brugernavnet allerede er i brug
    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    //Returnerer alle brugernavne
    public List<String> getAllUsernames() {
        return userRepository.findAll().stream()
                .map(User::getUsername)
                .toList();
    }

    // Sletter en bruger
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }


     // Ændrer/opdaterer rolle for en eksisterende bruger.

    public void assignRoleToUser(String username, String newRole) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setRole(newRole);
            userRepository.updateUser(user);
        }
    }


     // Undtagelse-klasse til auth-fejl.
    public static class AuthException extends Exception {
        public AuthException(String message) {
            super(message);
        }
    }
}


