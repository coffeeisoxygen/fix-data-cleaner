package com.coffeecode.parser.csv;

import java.io.File;
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
    private File file;

    public CSVParser(CSVLibrary csvLibrary, CSVConfig config) {
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
            csvLibrary.initialize(file, config);
            List<String> row;
            while ((row = csvLibrary.readNext()) != null) {
                container.addRow(row);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getMessage(), ERROR_PARSE, e);
        } finally {
            csvLibrary.close();
        }
    }

    private void validateFile() throws CustomException {
        if (!isValid()) {
            throw new CustomException("Invalid CSV file", ERROR_FILE);
        }
    }

    @Override
    public boolean isValid() {
        return file != null
                && file.exists()
                && file.canRead()
                && file.getName().toLowerCase().endsWith(".csv");
    }

    @Override
    public ParserType getType() {
        return ParserType.CSV;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
