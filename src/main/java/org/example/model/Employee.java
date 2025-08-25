package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Employee {

    private final int id;

    private final String name;

    private final double salary;

    private final int managerId;
}
