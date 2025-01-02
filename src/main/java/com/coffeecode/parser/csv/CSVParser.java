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
                container.addRow(row);
            }
        } catch (CustomException e) {
            logger.error("Failed to parse CSV file", e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during CSV parsing", e);
            throw new CustomException("Failed to parse CSV content", ERROR_PARSE, e);
        } finally {
            csvLibrary.close();
        }
    }

    private void validateFile() throws CustomException {
        if (!isValid()) {
            throw new CustomException("Invalid CSV file", ERROR_FILE);
        }
        logger.info("CSV file validated successfully: " + path.getFileName());
    }

    @Override
    public boolean isValid() {
        return path != null
                && Files.exists(path)
                && Files.isReadable(path)
                && path.toString().toLowerCase().endsWith(".csv");
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
