package gui;


//Enum-class, der definerer FXML-stier og CSS
public enum View {
    ADMIN("/gui/AdminView.fxml"),
    OPERATOR("/gui/OperatorView.fxml"),
    QA("/gui/QAView.fxml"),
    ROLE_SELECTION("/gui/RoleSelectionView.fxml"),
    ASSIGN_ROLE("/gui/AssignRoleView.fxml"),
    CREATE_USER("/gui/CreateUserView.fxml"),
    HISTORIK("/gui/Historik.fxml"),
    LOGIN("/gui/LoginView.fxml");

    private final String fxmlPath;
    private static final String CSS_PATH = "/gui/Style.css";

    View(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }


    // Returnerer stien til FXML
    public String getFxmlPath() {
        return fxmlPath;
    }


    //Returnerer stien til CSS
    public String getCssPath() {
        return CSS_PATH;
    }
}
