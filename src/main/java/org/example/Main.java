package org.example;

import org.example.model.CommandLineArguments;
import org.example.service.parser.ArgumentParser;
import org.example.service.file.FileProcessor;
import org.example.service.statistics.StatisticsGenerator;
import org.example.service.statistics.StatisticsWriter;

import java.io.IOException;

import static org.example.constants.ErrorMessages.*;


public class Main {
    public static void main(String[] args) {
        try {
            CommandLineArguments arguments = ArgumentParser.parse(args);
            FileProcessor fileProcessor = new FileProcessor();
            fileProcessor.processFiles(arguments);

            if (arguments.isStatEnabled()) {
                StatisticsGenerator statisticsGenerator = new StatisticsGenerator();
                String statistics = statisticsGenerator.generateStatistics(fileProcessor.getEmployeesByDepartment());
                StatisticsWriter.writeStatistics(statistics, arguments.getOutputMode(), arguments.getOutputPath());
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
