package com.coffeecode.parser.csv;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import com.coffeecode.exception.CustomException;
import com.coffeecode.logger.GeneralLogging;
import com.coffeecode.parser.csv.config.CSVConfig;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

public class OpenCSVLibrary implements CSVLibrary {

    private static final String ERROR_INIT = "CSV_INIT_ERROR";
    private static final String ERROR_READ = "CSV_READ_ERROR";
    private static final String ERROR_CLOSE = "CSV_CLOSE_ERROR";

    private final GeneralLogging logger;
    private CSVReader csvReader;
    private String headerMarker;

    public OpenCSVLibrary() {
        this.logger = new GeneralLogging(this.getClass());
    }

    @Override
    public void initialize(Path path, CSVConfig config) throws CustomException {
        try {
            this.headerMarker = config.getHeaderMarker();
            csvReader = new CSVReaderBuilder(
                    Files.newBufferedReader(path, Charset.forName(config.getCharset())))
                    .withCSVParser(new CSVParserBuilder()
                            .withSeparator(config.getSeparator())
                            .withQuoteChar(config.getQuoteChar())
                            .build())
                    .build(); // Remove skip lines as we handle it in parser
            logger.info("CSV reader initialized with header marker: " + headerMarker);
        } catch (IOException e) {
            throw new CustomException("Failed to initialize CSV reader", ERROR_INIT, e);
        }
    }

    @Override
    public List<String> readNext() throws CustomException {
        try {
            String[] line = csvReader.readNext();
            if (line != null && line.length > 0 && line[0] != null && line[0].startsWith("\uFEFF")) {
                // Check and remove BOM from the first element
                line[0] = line[0].substring(1); // Remove BOM if present
            }
            return line != null ? Arrays.asList(line) : null;
        } catch (IOException | CsvValidationException e) {
            throw new CustomException("Failed to read CSV line", ERROR_READ, e);
        }
    }

    @Override
    public void close() throws CustomException {
        try {
            if (csvReader != null) {
                csvReader.close();
                logger.info("CSV reader closed successfully");
            }
        } catch (IOException e) {
            throw new CustomException("Failed to close CSV reader", ERROR_CLOSE, e);
        }
    }

    @Override
    public boolean isHeaderLine(List<String> line) {
        return line != null
                && !line.isEmpty()
                && line.stream()
                        .anyMatch(field -> field != null
                        && field.trim().contains(headerMarker));
    }

    @Override
    public boolean isBlankLine(List<String> line) {
        return line == null
                || line.isEmpty()
                || line.stream()
                        .allMatch(field -> field == null
                        || field.trim().isEmpty());
    }
}
