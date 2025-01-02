package com.coffeecode.parser.csv;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.coffeecode.exception.CustomException;
import com.coffeecode.logger.GeneralLogging;
import com.coffeecode.model.DataContainer;
import com.coffeecode.parser.FileParser;
import com.coffeecode.parser.csv.config.CSVConfig;

public class CSVParser implements FileParser {

    private static final String ERROR_FILE = "CSV_FILE_ERROR";
    private static final String ERROR_PARSE = "CSV_PARSE_ERROR";

    private final CSVLibrary csvLibrary;
    private final CSVConfig config;
    private final DataContainer container;
    private final GeneralLogging logger;
    private Path path;

    public CSVParser(CSVLibrary csvLibrary, CSVConfig config) {
        this.csvLibrary = csvLibrary;
        this.config = config;
        this.container = new DataContainer();
        this.logger = new GeneralLogging(this.getClass());
    }

    @Override
    public void parse() throws CustomException {
        validateFile();
        try {
            csvLibrary.initialize(path, config);
            processFileContent();
        } catch (CustomException e) {
            logger.error("Failed to parse CSV file: " + path.getFileName(), e);
            throw new CustomException("Failed to parse CSV file", ERROR_PARSE, e);
        } finally {
            close();
        }
    }

    private void validateFile() throws CustomException {
        if (!isValid()) {
            throw new CustomException("Invalid CSV file", ERROR_FILE);
        }
        logger.info("CSV file validated successfully: " + path.getFileName());
    }

    private void processFileContent() throws CustomException {
        List<String> line;
        boolean headerFound = false;
        int lineCount = 0;

        logger.info("Starting to process file: " + path.getFileName());
        
        // Process until header
        while ((line = csvLibrary.readNext()) != null && !headerFound) {
            lineCount++;
            if (!csvLibrary.isBlankLine(line)) {
                if (csvLibrary.isHeaderLine(line)) {
                    headerFound = true;
                    container.setHeaders(line);
                    logger.info("Header found at line: " + lineCount);
                } else {
                    container.addMetadata(line);
                }
            }
        }

        if (!headerFound) {
            throw new CustomException("No header found in file", ERROR_PARSE);
        }

        // Process content
        while ((line = csvLibrary.readNext()) != null) {
            lineCount++;
            if (!csvLibrary.isBlankLine(line)) {
                container.addContent(line);
            }
        }

        logger.info("File processing completed. Total lines: " + lineCount);
    }

    @Override
    public void setPath(Path path) throws CustomException {
        if (path == null) {
            throw new CustomException("Path cannot be null", ERROR_FILE);
        }
        this.path = path;
        logger.info("Path set: " + path.getFileName());
    }

    @Override
    public boolean isValid() {
        return path != null
                && Files.exists(path)
                && Files.isReadable(path)
                && path.toString().toLowerCase().endsWith(".csv");
    }

    @Override
    public DataContainer getResult() {
        return container;
    }

    @Override
    public void close() throws CustomException {
        csvLibrary.close();
    }
}
