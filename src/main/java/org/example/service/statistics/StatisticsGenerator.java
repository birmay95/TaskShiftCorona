package org.example.service.statistics;

import org.example.model.Employee;

import java.util.*;

import static org.example.constants.FileFormatConstants.*;

public class StatisticsGenerator {

    private final Map<String, List<Employee>> employeesByDepartments;

    public StatisticsGenerator(Map<String, List<Employee>> employeesByDepartments) {
        this.employeesByDepartments = employeesByDepartments;
    }

    public String generateStatistics() {
        StringBuilder statistics = new StringBuilder();
        statistics.append(STAT_HEADER).append(LINE_SEPARATOR);

        for (String department : new TreeSet<>(employeesByDepartments.keySet())) {
            List<Employee> employees = employeesByDepartments.get(department);
            List<Double> salaries = extractSalaries(employees);
            double min = 0, max = 0, mid = 0;
            if (!salaries.isEmpty()) {
                min = Collections.min(salaries);
                max = Collections.max(salaries);
                mid = salaries.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            }
            appendDepartmentStatistics(statistics, department, min, max, mid);
        }

        return statistics.toString();
    }

    private List<Double> extractSalaries(List<Employee> employees) {
        List<Double> salaries = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getSalary() > 0) {
                salaries.add(employee.getSalary());
            }
        }
        return salaries;
    }

    private void appendDepartmentStatistics(StringBuilder sb, String department, double min, double max, double mid) {
        sb.append(department)
                .append(STRING_SEPARATOR + SINGLE_SPACE)
                .append(String.format(Locale.US, SALARY_FORMAT, min))
                .append(STRING_SEPARATOR + SINGLE_SPACE)
                .append(String.format(Locale.US, SALARY_FORMAT, max))
                .append(STRING_SEPARATOR + SINGLE_SPACE)
                .append(String.format(Locale.US, SALARY_FORMAT, mid))
                .append(LINE_SEPARATOR);
    }
}
