package org.example.constants;

public final class ErrorMessages {

    public static final String ARGUMENT_ERROR = "Ошибка параметров: ";

    public static final String IO_ERROR = "Ошибка ввода/вывода: ";

    public static final String UNEXPECTED_ERROR = "Непредвиденная ошибка: ";

    public static final String UNKNOWN_PARAM = "Неизвестный параметр: ";

    public static final String REQUIRED_VALUE_PARAM = "Параметр требует значение: ";

    public static final String INVALID_SORT_VALUE = "Неверное значение --sort: ";

    public static final String ORDER_WITHOUT_SORT = "--order допустим только при указании --sort";

    public static final String INVALID_ORDER_VALUE = "Неверное значение --order: ";

    public static final String OUTPUT_WITHOUT_STAT = "--output допустим только при указании --stat";

    public static final String INVALID_OUTPUT_VALUE = "Неверное значение --output: ";

    public static final String PATH_WITHOUT_STAT = "--path допустим только при указании --stat";

    public static final String PATH_WITHOUT_FILE_OUTPUT = "--path допустим только при --output=file";

    public static final String PATH_REQUIRED = "При --output=file необходимо указать --path";

    private ErrorMessages() {
    }
}
