package bll;

import dal.UserRepository;
import model.User;

/**
 * AuthService håndterer login-logik.
 */
public class AuthService {
    private UserRepository userRepository;

    /**
     * Constructor med dependency injection.
     */
    public AuthService() {
        this.userRepository = new UserRepository();
    }

    /**
     * Tjekker om brugernavn og password matcher en bruger i databasen.
     * @param username Brugernavn.
     * @param password Password.
     * @return Brugerens rolle, hvis login er succes; ellers null.
     */
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return user.getRole(); // Returnér rollen hvis korrekt login
            } else {
                System.out.println("Forkert password.");
            }
        } else {
            System.out.println("Bruger ikke fundet.");
        }
        return null;
    }
}
