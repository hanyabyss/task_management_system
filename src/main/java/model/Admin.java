package model;

public class Admin extends User {
    public Admin(String name) {
        super(name);
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Admin");
    }
}


