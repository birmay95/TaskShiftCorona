package org.example.service.file;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.example.constants.FileFormatConstants.ERROR_LOG;

public class ErrorWriter {

    public static void writeErrors(List<String> errorLines) throws IOException {
        Path errorFile = Paths.get(ERROR_LOG);
        if (errorLines.isEmpty()) {
            Files.deleteIfExists(errorFile);
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(errorFile, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (String err : errorLines) {
                writer.write(err);
                writer.newLine();
            }
        }
    }
}
