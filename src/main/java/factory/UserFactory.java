package factory;

import model.Admin;
import model.Developer;
import model.Tester;
import model.User;

public class UserFactory {
    public static User createUser(String role, String name) {
        switch (role) {
            case "Admin":
                return new Admin(name);
            case "Developer":
                return new Developer(name);
            case "Tester":
                return new Tester(name);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}

