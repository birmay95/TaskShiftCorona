package org.example.service.file;

import org.example.model.Arguments;
import org.example.model.Employee;
import org.example.model.Manager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.example.constants.FileFormatConstants.*;
import static org.example.constants.ParserConstants.ORDER_DESC;
import static org.example.constants.ParserConstants.SORT_BY_NAME;
import static org.example.constants.RecordTypeConstants.EMPLOYEE;
import static org.example.constants.RecordTypeConstants.MANAGER;

public class DepartmentWriter {

    public static void writeDepartments(Arguments arguments,
                                        Map<String, List<Employee>> employeesByDepartment,
                                        Map<String, Manager> managersByDepartment) throws IOException {
        for (String department : employeesByDepartment.keySet()) {
            writeDepartmentFile(arguments, department, employeesByDepartment.get(department), managersByDepartment.get(department));
        }
    }

    private static void writeDepartmentFile(Arguments arguments, String department, List<Employee> employees, Manager manager) throws IOException {
        List<Employee> sortedEmployees = sortEmployees(arguments, employees);

        Path outFile = Paths.get(department + FILE_EXTENSION);
        try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write(renderManagerRaw(manager));
            writer.newLine();

            for (Employee employee : sortedEmployees) {
                writer.write(renderEmployeeRaw(employee));
                writer.newLine();
            }
        }
    }

    private static List<Employee> sortEmployees(Arguments arguments, List<Employee> employees) {
        if (arguments.getSortField() == null) return employees;

        Comparator<Employee> comparator = SORT_BY_NAME.equals(arguments.getSortField())
                ? Comparator.comparing(Employee::getName, String.CASE_INSENSITIVE_ORDER)
                : Comparator.comparing(Employee::getSalary);

        if (ORDER_DESC.equals(arguments.getOrder())) {
            comparator = comparator.reversed();
        }

        employees.sort(comparator);
        return employees;
    }

    public static String renderEmployeeRaw(Employee employee) {
        return EMPLOYEE + STRING_SEPARATOR + employee.getId() + STRING_SEPARATOR + employee.getName() + STRING_SEPARATOR + formatSalary(employee.getSalary()) + STRING_SEPARATOR + employee.getManagerId();
    }

    public static String renderManagerRaw(Manager manager) {
        return MANAGER + STRING_SEPARATOR + manager.getId() + STRING_SEPARATOR + manager.getName() + STRING_SEPARATOR + formatSalary(manager.getSalary());
    }

    private static String formatSalary(double salary) {
        return (salary == Math.floor(salary))
                ? String.valueOf((int) salary)
                : String.format(Locale.US, SALARY_FORMAT, salary);
    }
}
