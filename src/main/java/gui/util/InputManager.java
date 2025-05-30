package gui.util;

/**
 * Hjælpeklasse til validering af brugerinput.
 */
public final class InputManager {
    private InputManager() { }

    /**
     * Tjekker at alle værdier ikke er null eller tomme.
     */
    public static boolean isFilled(String... values) {
        for (String v : values) {
            if (v == null || v.isBlank()) {
                return false;
            }
        }
        return true;
    }
}
