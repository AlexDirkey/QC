package gui.util;

import model.*;
//Singleton til at holde en bruger logget på

public final class SessionService {
    private static final SessionService instance = new SessionService();
    private volatile User currentUser;

    private SessionService() {}

    public static SessionService getInstance() { return instance; }

    public void login(User user) { this.currentUser = user; }
    public User current () { return currentUser; }
    public String currentUsername() { return currentUser != null ? currentUser.getUsername(): null; }
    public String currentRole() { return currentUser != null ? currentUser.getRole(): null; }
    public boolean isLoggedIn() { return currentUser != null; }
    public void logout() { this.currentUser = null; }
}
