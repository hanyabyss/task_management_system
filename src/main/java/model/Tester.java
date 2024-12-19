package model;

public class Tester extends User {
    public Tester(String name) {
        super(name);
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Tester");
    }
}

