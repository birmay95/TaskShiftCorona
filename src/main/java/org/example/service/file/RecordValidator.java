package org.example.service.file;

import org.example.model.Employee;
import org.example.model.Manager;

import static org.example.constants.FileFormatConstants.*;

public class RecordValidator {

    public static Manager validateManager(String[] parts) {
        Integer id = parsePositiveInt(parts[1]);
        String name = normalizeName(parts[2]);
        Double salary = parsePositiveDouble(parts[3]);
        String department = parts[4].trim();

        if (id == null || name.isEmpty() || salary == null || department.isEmpty()) {
            return null;
        }
        return new Manager(id, name, salary, department);
    }

    public static Employee validateEmployee(String[] parts) {
        Integer id = parsePositiveInt(parts[1]);
        String name = normalizeName(parts[2]);
        Double salary = parsePositiveDouble(parts[3]);
        Integer managerId = parsePositiveInt(parts[4]);

        if (id == null || name.isEmpty() || salary == null || managerId == null) {
            return null;
        }
        return new Employee(id, name, salary, managerId);
    }

    private static String normalizeName(String name) {
        return name == null ? EMPTY_STRING : name.trim().replaceAll(MULTIPLE_WHITESPACES_REGEX, SINGLE_SPACE);
    }

    private static Integer parsePositiveInt(String string) {
        try {
            int value = Integer.parseInt(string.trim());
            return value > 0 ? value : null;
        } catch (Exception exception) {
            return null;
        }
    }

    private static Double parsePositiveDouble(String string) {
        if (string == null || string.isBlank() || string.contains(STRING_SEPARATOR)) return null;
        try {
            double value = Double.parseDouble(string.trim());
            return value > 0.0 ? value : null;
        } catch (Exception exception) {
            return null;
        }
    }
}