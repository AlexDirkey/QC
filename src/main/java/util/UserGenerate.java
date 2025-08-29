package util;

import org.mindrot.jbcrypt.BCrypt;

//Brugt til at lave 3 testbrugere med startdata i databasen.
//KUN TIL DEMO/TEST

public class UserGenerate {
    public static void main(String[] args) {
        System.out.println("op1 hash: " + BCrypt.hashpw("pw1", BCrypt.gensalt()));
        System.out.println("qa1 hash: " + BCrypt.hashpw("pw2", BCrypt.gensalt()));
        System.out.println("admin1 hash: " + BCrypt.hashpw("pw3", BCrypt.gensalt()));
    }
}