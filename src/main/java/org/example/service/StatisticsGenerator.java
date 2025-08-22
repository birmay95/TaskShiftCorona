package org.example.service;

import org.example.model.Employee;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StatisticsGenerator {
    private final Map<String, List<Employee>> departments;

    public StatisticsGenerator(Map<String, List<Employee>> departments) {
        this.departments = departments;
    }

    public void generate(String outputMode, String outputPath) throws IOException {
        StringBuilder statistics = new StringBuilder();
        statistics.append("department, min, max, mid\n");

        for (String dep : new TreeSet<>(departments.keySet())) {
            List<Employee> employees = departments.get(dep);
            List<Double> salaries = new ArrayList<>();
            for (Employee employee : employees) {
                if (employee.getSalary() != 0) {
                    salaries.add(employee.getSalary());
                }
            }
            double min = 0, max = 0, mid = 0;
            if (!salaries.isEmpty()) {
                min = Collections.min(salaries);
                max = Collections.max(salaries);
                mid = salaries.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            }
            statistics.append(dep)
                    .append(", ")
                    .append(String.format(Locale.US, "%.2f", min))
                    .append(", ")
                    .append(String.format(Locale.US, "%.2f", max))
                    .append(", ")
                    .append(String.format(Locale.US, "%.2f", mid))
                    .append("\n");
        }

        if ("file".equals(outputMode)) {
            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(outputPath))) {
                bw.write(statistics.toString());
            }
        } else {
            System.out.println(statistics);
        }
    }
}
