package com.coffeecode.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.coffeecode.exception.CustomException;
import com.coffeecode.logger.GeneralLogging;
import com.coffeecode.logger.TechnicalLogging;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

public class CSVProcessor implements FileProcessor {
    private static final String ERROR_CODE_CSV_READ = "CSV_READ_ERROR";
    private static final String ERROR_CODE_CSV_INVALID = "CSV_INVALID_FILE";

    private final GeneralLogging logger;
    private final TechnicalLogging techLogger;
    private File file;
    private CSVReader csvReader;

    public CSVProcessor() {
        this.logger = new GeneralLogging(CSVProcessor.class);
        this.techLogger = new TechnicalLogging(CSVProcessor.class);
    }

    @Override
    public boolean validateFile(File file) {
        this.file = file;
        String fileName = file.getName().toLowerCase();
        boolean isValid = fileName.endsWith(".csv");

        if (!isValid) {
            logger.error("Invalid file type: " + fileName);
            try {
                throw new CustomException("File must be CSV format", ERROR_CODE_CSV_INVALID);
            } catch (CustomException e) {
                logger.error("Failed to validate CSV file", e);
            }
        }

        logger.info("Validated CSV file: " + fileName);
        return true;
    }

    @Override
    public Iterator<List<String>> getIterator() throws CustomException {
        try {
            techLogger.logThreadDetails();
            csvReader = new CSVReaderBuilder(new FileReader(file))
                    .build();

            return new Iterator<List<String>>() {
                private String[] nextLine;

                @Override
                public boolean hasNext() {
                    try {
                        nextLine = csvReader.readNext();
                        return nextLine != null;
                    } catch (IOException | CsvValidationException e) {
                        logger.error("Error reading CSV line", e);
                        return false;
                    }
                }

                @Override
                public List<String> next() {
                    if (nextLine == null) {
                        throw new NoSuchElementException("No more elements in the CSV file");
                    }
                    return new ArrayList<>(List.of(nextLine));
                }
            };
        } catch (FileNotFoundException e) {
            logger.error("Failed to create CSV iterator", e);
            throw new CustomException("Error reading CSV file", ERROR_CODE_CSV_READ, e);
        }
    }

    @Override
    public List<List<String>> readToArray() {
        List<List<String>> data = new ArrayList<>();
        techLogger.logExecutionTime("CSV Reading", () -> {
            try {
                Iterator<List<String>> iterator = getIterator();
                while (iterator.hasNext()) {
                    data.add(iterator.next());
                }
            } catch (CustomException e) {
                logger.error("Failed to read CSV to array", e);
            }
        });
        techLogger.logMemoryUsage();
        return data;
    }
}