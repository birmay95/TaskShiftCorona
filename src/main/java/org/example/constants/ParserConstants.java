package org.example.constants;

import java.util.Set;

public class ParserConstants {

    public static final String OUTPUT_MODE_FILE = "file";

    public static final String OUTPUT_MODE_CONSOLE = "console";

    public static final String ORDER_DESC = "desc";

    public static final String ORDER_ASC = "asc";

    public static final String SORT_BY_NAME = "name";

    public static final String SORT_BY_SALARY = "salary";

    public static final String ARG_SORT_LONG = "--sort";

    public static final String ARG_SORT_SHORT = "-s";

    public static final String ARG_ORDER = "--order";

    public static final String ARG_STAT = "--stat";

    public static final String ARG_OUTPUT_LONG = "--output";

    public static final String ARG_OUTPUT_SHORT = "-o";

    public static final String ARG_PATH = "--path";

    public static final String LONG_ARG_PREFIX = "--";

    public static final String SHORT_ARG_PREFIX = "-";

    public static final String KEY_VALUE_SEPARATOR = "=";

    public static final int MAX_SPLIT_PARTS = 2;

    public static final Set<String> ALLOWED_ARGS = Set.of(
            ARG_SORT_LONG, ARG_SORT_SHORT, ARG_ORDER, ARG_STAT, ARG_OUTPUT_LONG, ARG_OUTPUT_SHORT, ARG_PATH
    );

    public static final Set<String> REQUIRED_VALUE_ARGS = Set.of(
            ARG_SORT_LONG, ARG_SORT_SHORT,
            ARG_ORDER,
            ARG_OUTPUT_LONG, ARG_OUTPUT_SHORT,
            ARG_PATH
    );

    public static final Set<String> SORT_FIELDS = Set.of(SORT_BY_NAME, SORT_BY_SALARY);

    public static final Set<String> ORDER_VALUES = Set.of(ORDER_ASC, ORDER_DESC);

    public static final Set<String> OUTPUT_MODES = Set.of(OUTPUT_MODE_CONSOLE, OUTPUT_MODE_FILE);

    public static final String ALLOWED_SORT_FIELDS_MESSAGE = "Допустимые значения: " + String.join(", ", SORT_FIELDS);

    public static final String ALLOWED_ORDER_VALUES_MESSAGE = "Допустимые значения: " + String.join(", ", ORDER_VALUES);

    public static final String ALLOWED_OUTPUT_MODES_MESSAGE = "Допустимые значения: " + String.join(", ", OUTPUT_MODES);

    public static final String ALLOWED_ARGS_MESSAGE = "Допустимые значения: " + String.join(", ", ALLOWED_ARGS);

    private ParserConstants() {
    }
}
