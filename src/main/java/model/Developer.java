package model;

public class Developer extends User {
    public Developer(String name) {
        super(name);
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Developer");
    }
}


