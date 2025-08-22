package org.example.service;

import org.example.model.Employee;
import org.example.model.Manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class FileProcessor {
    private final ArgumentParser parser;

    private final Map<String, List<Employee>> employeesByDepartment = new TreeMap<>();

    private final Map<String, Manager> managersByDepartment = new HashMap<>();

    private final Map<Integer, Manager> managersById = new HashMap<>();

    private final List<Employee> allEmployees = new ArrayList<>();

    private final List<String> errors = new ArrayList<>();

    private final Set<Integer> usedIds = new HashSet<>();

    public FileProcessor(ArgumentParser parser) {
        this.parser = parser;
    }

    public void processFiles() throws IOException {
        List<Path> files = Files.list(Paths.get("."))
                .filter(file -> !Files.isDirectory(file))
                .filter(file -> file.getFileName().toString().toLowerCase().endsWith(".sb"))
                .sorted(Comparator.comparing(file -> file.getFileName().toString(), String.CASE_INSENSITIVE_ORDER))
                .toList();

        for (Path path : files) {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                String raw;
                while ((raw = reader.readLine()) != null) {
                    processRawLine(raw);
                }
            }
        }

        for (Employee employee : allEmployees) {
            Manager manager = managersById.get(employee.getManagerId());
            if (manager == null) {
                errors.add(renderEmployeeRaw(employee));
                continue;
            }

            if (!usedIds.add(employee.getId())) {
                errors.add(renderEmployeeRaw(employee));
                continue;
            }

            for (Map.Entry<String, Manager> entry : managersByDepartment.entrySet()) {
                if (entry.getValue().getId() == manager.getId()) {
                    employeesByDepartment.get(entry.getKey()).add(employee);
                }
            }
        }

        writeDepartments();
        writeErrors();
    }

    private void processRawLine(String rawLine) {
        String line = rawLine == null ? "" : rawLine.trim();
        if (line.isEmpty()) return;

        String[] parts = Arrays.stream(line.split(",", -1))
                .map(String::trim)
                .toArray(String[]::new);

        if (parts.length != 5) {
            errors.add(rawLine);
            return;
        }

        String type = parts[0];
        if ("Manager".equals(type)) {
            handleManager(parts, rawLine);
        } else if ("Employee".equals(type)) {
            handleEmployee(parts, rawLine);
        } else {
            errors.add(rawLine);
        }
    }

    private void handleManager(String[] parts, String rawLine) {
        Integer id = parsePositiveInt(parts[1]);
        String name = parts[2].trim().replaceAll("\\s+", " ");
        Double salary = parsePositiveDouble(parts[3]);
        String dep = parts[4].trim();

        if (id == null || name.isEmpty() || salary == null || dep.isEmpty()) {
            errors.add(rawLine);
            return;
        }
        if (managersById.containsKey(id)) {
            Manager existing = managersById.get(id);
            if (!existing.getName().equals(name)) {
                errors.add(rawLine);
                return;
            }
        }
        usedIds.add(id);
        if (managersByDepartment.containsKey(dep)) {
            errors.add(rawLine);
            return;
        }

        Manager m = new Manager(id, name, salary, dep);
        managersByDepartment.put(dep, m);
        managersById.put(id, m);
        employeesByDepartment.computeIfAbsent(dep, k -> new ArrayList<>());
    }

    private void handleEmployee(String[] parts, String rawLine) {
        Integer id = parsePositiveInt(parts[1]);
        String name = parts[2].trim().replaceAll("\\s+", " ");
        Double salary = parsePositiveDouble(parts[3]);   // > 0 по требованиям
        Integer managerId = parsePositiveInt(parts[4]);

        if (id == null || name.isEmpty() || salary == null || managerId == null) {
            errors.add(rawLine);
            return;
        }

        allEmployees.add(new Employee(id, name, salary, managerId));
    }

    private Integer parsePositiveInt(String s) {
        try {
            int v = Integer.parseInt(s.trim());
            return v > 0 ? v : null;
        } catch (Exception e) {
            return null;
        }
    }

    private Double parsePositiveDouble(String s) {
        if (s == null || s.isBlank() || s.contains(",")) return null;
        try {
            double v = Double.parseDouble(s.trim());
            return v > 0.0 ? v : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String renderEmployeeRaw(Employee e) {
        return "Employee," + e.getId() + "," + e.getName() + "," + formatSalary(e.getSalary()) + "," + e.getManagerId();
    }

    private String formatSalary(double salary) {
        if (salary == Math.floor(salary)) {
            return String.valueOf((int) salary);
        } else {
            return String.format(Locale.US, "%.2f", salary);
        }
    }

    private void writeDepartments() throws IOException {
        for (String department : employeesByDepartment.keySet()) {
            Manager manager = managersByDepartment.get(department);

            List<Employee> employees = new ArrayList<>(employeesByDepartment.get(department));

            if (parser.getSortField() != null) {
                Comparator<Employee> comparator;
                if ("name".equals(parser.getSortField())) {
                    comparator = Comparator.comparing(Employee::getName, String.CASE_INSENSITIVE_ORDER);
                } else {
                    comparator = Comparator.comparing(Employee::getSalary);
                }
                if ("desc".equals(parser.getOrder())) {
                    comparator = comparator.reversed();
                }
                employees.sort(comparator);
            }

            Path outFile = Paths.get(department + ".sb");
            try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                writer.write("Manager," + manager.getId() + "," + manager.getName() + "," + formatSalary(manager.getSalary()));
                writer.newLine();

                for (Employee employee : employees) {
                    writer.write("Employee," + employee.getId() + "," + employee.getName() + "," + formatSalary(employee.getSalary()) + "," + employee.getManagerId());
                    writer.newLine();
                }
            }
        }
    }

    private void writeErrors() throws IOException {
        if (errors.isEmpty()) {
            Files.deleteIfExists(Paths.get("error.log"));
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("error.log"),
                StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (String err : errors) {
                writer.write(err);
                writer.newLine();
            }
        }
    }

    public Map<String, List<Employee>> getEmployeesByDepartment() {
        return employeesByDepartment;
    }
}
