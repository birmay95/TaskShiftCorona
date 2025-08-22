package org.example;

import org.example.service.ArgumentParser;
import org.example.service.FileProcessor;
import org.example.service.StatisticsGenerator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ArgumentParser parser = new ArgumentParser(args);
            FileProcessor processor = new FileProcessor(parser);
            processor.processFiles();

            if (parser.isStatEnabled()) {
                StatisticsGenerator statGen = new StatisticsGenerator(processor.getEmployeesByDepartment());
                statGen.generate(parser.getOutputMode(), parser.getOutputPath());
            }
        } catch (IllegalArgumentException exception) {
            System.err.println("Ошибка параметров: " + exception.getMessage());
        } catch (IOException exception) {
            System.err.println("Ошибка ввода/вывода: " + exception.getMessage());
        } catch (Exception exception) {
            System.err.println("Непредвиденная ошибка: " + exception.getMessage());
        }
    }
}
