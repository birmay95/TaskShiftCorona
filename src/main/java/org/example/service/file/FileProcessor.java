package org.example.service.file;

import lombok.Getter;
import org.example.model.CommandLineArguments;
import org.example.model.Employee;
import org.example.model.Manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import static org.example.constants.FileFormatConstants.*;
import static org.example.constants.RecordTypeConstants.EMPLOYEE;
import static org.example.constants.RecordTypeConstants.MANAGER;
import static org.example.service.file.DepartmentWriter.renderEmployeeRaw;
import static org.example.service.file.DepartmentWriter.writeDepartments;
import static org.example.service.file.ErrorWriter.writeErrors;
import static org.example.service.file.RecordValidator.validateEmployee;
import static org.example.service.file.RecordValidator.validateManager;

@Getter
public class FileProcessor {

    private final Map<String, List<Employee>> employeesByDepartment = new TreeMap<>();

    private final Map<String, Manager> managersByDepartment = new HashMap<>();

    private final Map<Integer, Manager> managersById = new HashMap<>();

    private final List<Employee> allEmployees = new ArrayList<>();

    private final List<String> errorLines = new ArrayList<>();

    private final Set<Integer> usedIds = new HashSet<>();

    public FileProcessor() {
    }

    public void processFiles(CommandLineArguments arguments) throws IOException {
        List<Path> files = findInputFiles();
        readFiles(files);
        assignEmployeesToDepartments();
        writeDepartments(arguments, employeesByDepartment, managersByDepartment);
        writeErrors(errorLines);
    }

    private List<Path> findInputFiles() throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(CURRENT_DIRECTORY))) {
            return stream
                    .filter(file -> !Files.isDirectory(file)
                            && file.getFileName().toString().toLowerCase().endsWith(FILE_EXTENSION))
                    .toList();
        }
    }

    private void readFiles(List<Path> files) throws IOException {
        for (Path path : files) {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                String raw;
                while ((raw = reader.readLine()) != null) {
                    processLine(raw);
                }
            }
        }
    }

    private void processLine(String line) {
        String[] parts = splitLine(line);
        if (parts == null) {
            errorLines.add(line);
            return;
        }

        String type = parts[0];
        switch (type) {
            case MANAGER -> processManager(parts, line);
            case EMPLOYEE -> processEmployee(parts, line);
            default -> errorLines.add(line);
        }
    }

    private String[] splitLine(String line) {
        if (line == null || line.isBlank()) return null;
        String[] parts = Arrays.stream(line.split(STRING_SEPARATOR, -1))
                .map(String::trim)
                .toArray(String[]::new);
        return parts.length == 5 ? parts : null;
    }

    private void processManager(String[] parts, String rawLine) {
        Manager manager = validateManager(parts);
        if (manager == null) {
            errorLines.add(rawLine);
            return;
        }

        if (managersById.containsKey(manager.getId())) {
            Manager existing = managersById.get(manager.getId());
            if (!existing.getName().equals(manager.getName())) {
                errorLines.add(rawLine);
                return;
            }
        }
        if (managersByDepartment.containsKey(manager.getDepartment())) {
            errorLines.add(rawLine);
            return;
        }

        usedIds.add(manager.getId());
        managersByDepartment.put(manager.getDepartment(), manager);
        managersById.put(manager.getId(), manager);
        employeesByDepartment.computeIfAbsent(manager.getDepartment(), k -> new ArrayList<>());
    }

    private void processEmployee(String[] parts, String rawLine) {
        Employee employee = validateEmployee(parts);
        if (employee == null) {
            errorLines.add(rawLine);
            return;
        }
        allEmployees.add(employee);
    }

    private void assignEmployeesToDepartments() {
        for (Employee employee : allEmployees) {
            Manager manager = managersById.get(employee.getManagerId());
            if (manager == null || !usedIds.add(employee.getId())) {
                errorLines.add(renderEmployeeRaw(employee));
                continue;
            }

            for (Map.Entry<String, Manager> entry : managersByDepartment.entrySet()) {
                if (entry.getValue().getId() == manager.getId()) {
                    employeesByDepartment.get(entry.getKey()).add(employee);
                }
            }
        }
    }
}
