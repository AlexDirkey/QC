package gui;


//Enum-class, der definerer FXML-stier og CSS
// Hver konstant er koblet til hver deres view

public enum View {
    ADMIN("/gui/AdminView.fxml"),
    OPERATOR("/gui/OperatorView.fxml"),
    QA("/gui/QAView.fxml"),
    ROLE_SELECTION("/gui/RoleSelectionView.fxml"),
    ASSIGN_ROLE("/gui/AssignRoleView.fxml"),
    CREATE_USER("/gui/CreateUserView.fxml"),
    HISTORIK("/gui/Historik.fxml"),
    LOGIN("/gui/LoginView.fxml");

    // Sti til den fxml-fil, der beskriver view'et

    private final String fxmlPath;

    // Fælles CSS-sti, der bruges på alle views

    private static final String CSS_PATH = "/gui/Style.css";


    // Konstruktør, der kobler hver enum-konstant til dens fxml-sti

    View(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }


    // Getter, der returnerer stien til enum

    public String getFxmlPath() {
        return fxmlPath;
    }


    //Getter, der returnerer stien til den fælles CSS-sti
    public String getCssPath() {
        return CSS_PATH;
    }
}
