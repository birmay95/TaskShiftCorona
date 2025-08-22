package org.example.model;

public class Employee {
    private final int id;
    private final String name;
    private final double salary;
    private final int managerId;

    public Employee(int id, String name, Double salary, int managerId) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.managerId = managerId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getSalary() {
        return salary;
    }

    public int getManagerId() {
        return managerId;
    }

    @Override
    public String toString() {
        return "Employee," + id + "," + name + "," + salary + "," + managerId;
    }
}
