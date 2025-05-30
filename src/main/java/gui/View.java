package gui;

/**
 * Enum der definerer FXML-stier og CSS-sti for de forskellige views.
 * Gør scene-skift mere typesikkert og DRY.
 */
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

    /**
     * @return stien til FXML-filen for dette view
     */
    public String getFxmlPath() {
        return fxmlPath;
    }

    /**
     * @return stien til fælles CSS-fil
     */
    public String getCssPath() {
        return CSS_PATH;
    }
}
