package org.example;

import org.example.model.Arguments;
import org.example.service.parser.ArgumentParser;
import org.example.service.file.FileProcessor;
import org.example.service.statistics.StatisticsGenerator;

import java.io.IOException;

import static org.example.constants.ErrorMessages.*;
import static org.example.service.statistics.StatisticsWriter.writeStatistics;


public class Main {
    public static void main(String[] args) {
        try {
            Arguments arguments = ArgumentParser.parse(args);
            FileProcessor processor = new FileProcessor();
            processor.processFiles(arguments);

            if (arguments.isStatEnabled()) {
                StatisticsGenerator statisticsGenerator = new StatisticsGenerator(processor.getEmployeesByDepartment());
                String statistics = statisticsGenerator.generateStatistics();
                writeStatistics(statistics, arguments.getOutputMode(), arguments.getOutputPath());
            }
        } catch (IllegalArgumentException exception) {
            System.err.println(ARGUMENT_ERROR + exception.getMessage());
        } catch (IOException exception) {
            System.err.println(IO_ERROR + exception.getMessage());
        } catch (Exception exception) {
            System.err.println(UNEXPECTED_ERROR + exception.getMessage());
        }
    }
}
