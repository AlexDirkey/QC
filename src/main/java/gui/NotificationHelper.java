package gui;

// Hjælpeklasse til dialoger/notifikationer i GUI.
// I stedet for at alle controllere selv kalder showWarning()/showInfo(),
// kan de bruge denne helper.
public class NotificationHelper {
    private final BaseController controller; // Reference til den controller der har dialogerne

    // Constructor: tager imod den controller, som skal kunne vise beskeder

    public NotificationHelper(BaseController controller) {
        this.controller = controller;
    }

    // wrapper-metode til at vise en advarsel

    public void showWarning(String title, String message) {
        controller.showWarning(title, message);
    }

    // wrapper-metode til at vise en info-dialog

    public void showInfo(String title, String message) {
        controller.showInfo(title, message);
    }

    // Specialiserede beskeder til typiske fejlscenarier:
    // Alle disse metoder bruger showWarning/showInfo men med faste tekster,
    // så controllere slipper for at gentage strengene.

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
