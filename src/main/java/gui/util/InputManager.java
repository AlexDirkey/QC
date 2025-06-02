package gui.util;


//Hjælperclass til brugerinput
public final class InputManager {
    private InputManager() { }


    //Checker, om alle værdier ikke er tomme. Eller null
    public static boolean isFilled(String... values) {
        for (String v : values) {
            if (v == null || v.isBlank()) {
                return false;
            }
        }
        return true;
    }
}
