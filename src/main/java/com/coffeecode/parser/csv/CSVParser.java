package com.coffeecode.parser.csv;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.coffeecode.exception.CustomException;
import com.coffeecode.parser.AbstractParser;
import com.coffeecode.parser.ParserType;
import com.coffeecode.parser.csv.config.CSVConfig;

public class CSVParser extends AbstractParser {

    private static final String ERROR_VALIDATE = "CSV_VALIDATE_ERROR";
    private static final String ERROR_PARSE = "CSV_PARSE_ERROR";
    private static final String ERROR_FILE = "CSV_FILE_ERROR";

    private final CSVLibrary csvLibrary;
    private final CSVConfig config;
    private Path path;

    public CSVParser(CSVLibrary csvLibrary, CSVConfig config) {
        super();
        this.csvLibrary = csvLibrary;
        this.config = config;
    }

    @Override
    protected void validateContainer() throws CustomException {
        if (container == null) {
            throw new CustomException("Data container is null", ERROR_VALIDATE);
        }
    }

    @Override
    protected void parseContent() throws CustomException {
        try {
            validateFile();
            csvLibrary.initialize(path, config);
            List<String> row;
            while ((row = csvLibrary.readNext()) != null) {
                if (!isEmptyRow(row)) {
                    container.addRow(row);
                }
            }
        } catch (CustomException e) {
            logger.error("Failed to parse CSV file", e);
            throw e;
        } finally {
            csvLibrary.close();
        }
    }

    private boolean isEmptyRow(List<String> row) {
        return row.stream()
                .map(String::trim)
                .allMatch(String::isEmpty);
    }

    private void validateFile() throws CustomException {
        if (!isValid()) {
            throw new CustomException("Invalid CSV file", ERROR_FILE);
        }
        logger.info("CSV file validated successfully: " + path.getFileName());
    }

    @Override
    public boolean isValid() {
        try {
            logger.debug("Validating file: " + path);
            boolean exists = path != null && Files.exists(path);
            boolean readable = exists && Files.isReadable(path);
            boolean isCsv = readable && path.toString().toLowerCase().endsWith(".csv");
            logger.debug("File exists: " + exists);
            logger.debug("File is readable: " + readable);
            logger.debug("File has .csv extension: " + isCsv);
            return isCsv;
        } catch (SecurityException e) {
            logger.error("Security error checking file", e);
            return false;
        }
    }

    @Override
    public ParserType getType() {
        return ParserType.CSV;
    }

    public void setPath(Path path) throws CustomException {
        if (path == null) {
            throw new CustomException("Path cannot be null", ERROR_FILE);
        }
        this.path = path;
        logger.info("Path set: " + path.getFileName());
    }
}
