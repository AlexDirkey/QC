package gui;


//Hjælperklasse til dialoger
public class NotificationHelper {
    private final BaseController controller;

    public NotificationHelper(BaseController controller) {
        this.controller = controller;
    }

    // Wrapper til  warning
    public void showWarning(String title, String message) {
        controller.showWarning(title, message);
    }

    // Wrapper til info
    public void showInfo(String title, String message) {
        controller.showInfo(title, message);
    }

    public void warnMissingInput() {
        controller.showWarning("Manglende input", "Udfyld alle felter for at fortsætte.");
    }

    public void warnUserExists(String username) {
        controller.showWarning("Eksisterende bruger", "Brugernavnet '" + username + "' er allerede i brug.");
    }

    public void infoUserCreated(String username, String role) {
        controller.showInfo("Bruger oprettet", "Bruger '" + username + "' blev oprettet som " + role + ".");
    }

    public void infoUserDeleted(String username) {
        controller.showInfo("Bruger slettet", "Bruger '" + username + "' er blevet slettet.");
    }

    public void warnSelectUser() {
        controller.showWarning("Ingen valgt", "Vælg en bruger først.");
    }

    public void warnSelectUserAndRole() {
        controller.showWarning("Manglende valg", "Vælg både en bruger og en rolle.");
    }
    public void infoRoleAssigned(String username, String role) {
        showInfo("Bruger opdateret", username + " er nu " + role);
    }
}
