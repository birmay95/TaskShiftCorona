package org.example.service.parser;

import org.example.model.CommandLineArguments;

import java.util.HashMap;
import java.util.Map;

import static org.example.constants.FileFormatConstants.LINE_SEPARATOR;
import static org.example.constants.ParserConstants.*;
import static org.example.constants.ErrorMessages.*;

public class ArgumentParser {

    public static CommandLineArguments parse(String[] args) {
        Map<String, String> params = parseArgsToMap(args);

        String sortField = parseSortField(params);
        String order = parseOrder(params, sortField);
        boolean statEnabled = params.containsKey(ARG_STAT);
        String outputMode = parseOutputMode(params, statEnabled);
        String outputPath = parseOutputPath(params, statEnabled, outputMode);

        return new CommandLineArguments(sortField, order, statEnabled, outputMode, outputPath);
    }

    private static Map<String, String> parseArgsToMap(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (String arg : args) {
            if (arg.startsWith(LONG_ARG_PREFIX) || arg.startsWith(SHORT_ARG_PREFIX)) {
                String[] parts = arg.split(KEY_VALUE_SEPARATOR, MAX_SPLIT_PARTS);
                String key = parts[0];
                if (!ALLOWED_ARGS.contains(key)) {
                    throw new IllegalArgumentException(UNKNOWN_PARAM + key + LINE_SEPARATOR + ALLOWED_ARGS_MESSAGE);
                }
                if (REQUIRED_VALUE_ARGS.contains(key)) {
                    if (parts.length < 2 || parts[1] == null || parts[1].isBlank()) {
                        throw new IllegalArgumentException(REQUIRED_VALUE_PARAM + key);
                    }
                }
                map.put(key, parts.length > 1 ? parts[1] : null);
            } else {
                throw new IllegalArgumentException(UNKNOWN_PARAM + arg + LINE_SEPARATOR + ALLOWED_ARGS_MESSAGE);
            }
        }
        return map;
    }

    private static String parseSortField(Map<String, String> params) {
        String sortField = params.getOrDefault(ARG_SORT_LONG, params.get(ARG_SORT_SHORT));
        if (sortField != null && !SORT_FIELDS.contains(sortField)) {
            throw new IllegalArgumentException(
                    INVALID_SORT_VALUE
                            + sortField
                            + LINE_SEPARATOR
                            + ALLOWED_SORT_FIELDS_MESSAGE
            );
        }
        return sortField;
    }

    private static String parseOrder(Map<String, String> params, String sortField) {
        String order = params.get(ARG_ORDER);
        if (order != null) {
            if (sortField == null) {
                throw new IllegalArgumentException(ORDER_WITHOUT_SORT);
            }
            if (!ORDER_VALUES.contains(order)) {
                throw new IllegalArgumentException(
                        INVALID_ORDER_VALUE
                                + order
                                + LINE_SEPARATOR
                                + ALLOWED_ORDER_VALUES_MESSAGE
                );
            }
        }
        return order;
    }

    private static String parseOutputMode(Map<String, String> params, boolean statEnabled) {
        String outputMode = params.getOrDefault(ARG_OUTPUT_LONG, params.get(ARG_OUTPUT_SHORT));
        if (outputMode != null) {
            if (!statEnabled) {
                throw new IllegalArgumentException(OUTPUT_WITHOUT_STAT);
            }
            if (!OUTPUT_MODES.contains(outputMode)) {
                throw new IllegalArgumentException(
                        INVALID_OUTPUT_VALUE
                                + outputMode
                                + LINE_SEPARATOR
                                + ALLOWED_OUTPUT_MODES_MESSAGE
                );
            }
        } else {
            outputMode = OUTPUT_MODE_CONSOLE;
        }
        return outputMode;
    }

    private static String parseOutputPath(Map<String, String> params, boolean statEnabled, String outputMode) {
        String path = params.get(ARG_PATH);
        if (path != null) {
            if (!statEnabled) {
                throw new IllegalArgumentException(PATH_WITHOUT_STAT);
            }
            if (!OUTPUT_MODE_FILE.equals(outputMode)) {
                throw new IllegalArgumentException(PATH_WITHOUT_FILE_OUTPUT);
            }
        }
        if (OUTPUT_MODE_FILE.equals(outputMode) && path == null) {
            throw new IllegalArgumentException(PATH_REQUIRED);
        }
        return path;
    }
}
