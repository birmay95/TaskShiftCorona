package org.example.service.statistics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.example.constants.ParserConstants.OUTPUT_MODE_FILE;

public class StatisticsWriter {

    public static void writeStatistics(String statistics, String outputMode, String outputPath) throws IOException {
        if (OUTPUT_MODE_FILE.equals(outputMode)) {
            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(outputPath))) {
                bw.write(statistics);
            }
        } else {
            System.out.println(statistics);
        }
    }
}
