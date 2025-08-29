package bll;

import model.User;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

// Enhedstest for AuthTest - for at teste login fungerer. Bruges for at dække de vigtigste situationer, et login kan komme ud for

public class AuthTest {

    //FakeUserRepository bruges til at "lade som om", at vi har en database, så vi kan teste uden forbindelse til den ægte database

    private static class FakeUserRepository extends dal.UserRepository {
        private final User testUser;

        // Der bliver lavet et test-bruger

        FakeUserRepository(User testUser) {
            this.testUser = testUser;
        }

        // Når AuthService kalder findByUserName-metoden, snyder vi med at kalde vores test-bruger istedet.
        //Returnerer null, hvis test-brugeren ikke kan findes

        @Override
        public User findByUsername(String username) {
            if (testUser != null && testUser.getUsername().equals(username)) {
                return testUser;
            }
            return null;
        }
    }

    // Første test - hvis brugernavn og kodeord er korrekt, skal authenticate returnere brugeren

    @Test
    void authenticate_withCorrectPassword_returnsUser() throws AuthService.AuthException {
        // Arrange
        String rawPassword = "secret123";
        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        User u = new User(1, "alice", hash, "OPERATOR");

        AuthService authService = new AuthService(new FakeUserRepository(u));

        // Act
        User result = authService.authenticate("alice", "secret123");

        // Assert
        assertNotNull(result, "Authenticate skal returnere en bruger");
        assertEquals("alice", result.getUsername());
        assertEquals("OPERATOR", result.getRole());
    }

    // Anden test - Passer kodeordet ikke, skal authenticate kaste en AuthException

    @Test
    void authenticate_withWrongPassword_throwsAuthException() {
        // Arrange
        String rawPassword = "secret123";
        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        User u = new User(1, "bob", hash, "QA");

        AuthService authService = new AuthService(new FakeUserRepository(u));

        // Act + Assert
        assertThrows(AuthService.AuthException.class,
                () -> authService.authenticate("bob", "forkert"),
                "Forkert password skal give exception");
    }

    //Tredje test - Kan brugeren ikke findes, skal authenticate også kaste en AuthException
    @Test
    void authenticate_withUnknownUser_throwsAuthException() {
        // Arrange
        AuthService authService = new AuthService(new FakeUserRepository(null));

        // Act + Assert
        assertThrows(AuthService.AuthException.class,
                () -> authService.authenticate("ukendt", "whatever"),
                "Ukendt bruger skal give exception");
    }
}
