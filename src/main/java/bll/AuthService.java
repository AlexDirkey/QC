package bll;

import dal.UserRepository;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

/**
 * AuthService håndterer login og brugeradministration.
 * Indeholder en indlejret undtagelse for auth-fejl.
 */
public class AuthService {
    private final UserRepository userRepository;

    /**
     * Standardkonstruktør, som bruger en ny UserRepository.
     */
    public AuthService() {
        this(new UserRepository());
    }

    /**
     * Dependency injection af UserRepository for bedre testbarhed.
     */
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Forsøger at logge en bruger ind.
     *
     * @param username Brugerens brugernavn
     * @param password Brugerens password
     * @throws AuthException hvis brugeren ikke findes eller login-fejl
     */
    public void authenticate(String username, String password) throws AuthException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AuthException("Bruger ikke oprettet – kontakt administrator.");
        }
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new AuthException("Forkert brugernavn eller password – kontakt administrator.");
        }
    }

    /**
     * Opretter en ny bruger med hashet password.
     */
    public void addUser(String username, String password, String role) {
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(0, username, hash, role);
        userRepository.save(user);
    }

    /**
     * Tjekker om et brugernavn allerede er i brug.
     */
    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    /**
     * Returnerer alle brugernavne i systemet.
     */
    public List<String> getAllUsernames() {
        return userRepository.findAll().stream()
                .map(User::getUsername)
                .toList();
    }

    /**
     * Sletter en bruger ud fra brugernavnet.
     */
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    /**
     * Ændrer rolle for en eksisterende bruger.
     */
    public void assignRoleToUser(String username, String newRole) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setRole(newRole);
            userRepository.updateUser(user);
        }
    }


     // Enkel undtagelse-klasse til auth-fejl.
    public static class AuthException extends Exception {
        public AuthException(String message) {
            super(message);
        }
    }
}
