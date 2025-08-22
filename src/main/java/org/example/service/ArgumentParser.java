package org.example.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ArgumentParser {
    private String sortField = null;
    private String order = null;
    private boolean statEnabled = false;
    private String outputMode = "console";
    private String outputPath = null;

    public ArgumentParser(String[] args) {
        Map<String, String> params = new HashMap<>();

        Set<String> allowedParams = Set.of("--sort", "-s", "--order", "--stat", "--output", "-o", "--path");

        for (String arg : args) {
            if (arg.startsWith("--") || arg.startsWith("-")) {
                String[] parts = arg.split("=", 2);
                String key = parts[0];

                if (!allowedParams.contains(key)) {
                    throw new IllegalArgumentException("Неизвестный параметр: " + key);
                }

                params.put(parts[0], parts.length > 1 ? parts[1] : null);
            } else {
                throw new IllegalArgumentException("Неизвестный параметр: " + arg);
            }
        }

        if (params.containsKey("--sort") || params.containsKey("-s")) {
            sortField = params.getOrDefault("--sort", params.get("-s"));
            if (!("name".equals(sortField) || "salary".equals(sortField))) {
                throw new IllegalArgumentException("Неверное значение --sort: " + sortField);
            }
        }

        if (params.containsKey("--order")) {
            order = params.get("--order");
            if (sortField == null) {
                throw new IllegalArgumentException("--order допустим только при указании --sort");
            }
            if (!("asc".equals(order) || "desc".equals(order))) {
                throw new IllegalArgumentException("Неверное значение --order: " + order);
            }
        }

        if (params.containsKey("--stat")) {
            statEnabled = true;
        }

        if (params.containsKey("--output") || params.containsKey("-o")) {
            if (!statEnabled) {
                throw new IllegalArgumentException("--output допустим только при указании --stat");
            }
            outputMode = params.getOrDefault("--output", params.get("-o"));
            if (!(outputMode.equals("console") || outputMode.equals("file"))) {
                throw new IllegalArgumentException("Неверное значение --output: " + outputMode);
            }
        }

        if (params.containsKey("--path")) {
            if (!statEnabled) {
                throw new IllegalArgumentException("--path допустим только при указании --stat");
            }
            outputPath = params.get("--path");
            if (!"file".equals(outputMode)) {
                throw new IllegalArgumentException("--path допустим только при --output=file");
            }
        }

        if ("file".equals(outputMode) && outputPath == null) {
            throw new IllegalArgumentException("При --output=file необходимо указать --path");
        }
    }

    public String getSortField() {
        return sortField;
    }

    public String getOrder() {
        return order;
    }

    public boolean isStatEnabled() {
        return statEnabled;
    }

    public String getOutputMode() {
        return outputMode;
    }

    public String getOutputPath() {
        return outputPath;
    }
}
