package model;
//Repræsenterer en bruger. Indeholder ID, brugernavn, password og rolle
public class User {
    private int id; // Primærnøgle
    private String username; // Brugernavn til Login
    private String password; // Password (hashet)
    private String role; //Rolle i systemet; QA, admin, operator

    //Construktor med alle felter, hvis vi henter en bruger direkte fra databasen

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    //Construktur, men uden id - brugt i forbindelse med ny bruger. Databasen sørger for id

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getter til id

    public int getId() {
        return id;
    }

    //Getter til brugernavn

    public String getUsername() {
        return username;
    }

    //Setter til  password (hashet)

    public String getPassword() {
        return password;
    }

    //Setter til rolle

    public String getRole() {
        return role;
    }

    // Setter til rolle, i tilfælde af AdminController skal oprette ny bruger

    public void setPassword(String password) {
        this.password = password;
    }

    //Setter til ny rolle - hvis admin laver ny bruger

    public void setRole(String role) {
        this.role = role;
    }

    // ToString metode, til repræsentation af brugere - bruges til at vise en liste over brugere i GUI

    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
}

