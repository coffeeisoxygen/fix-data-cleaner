package com.coffeecode.processor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.coffeecode.exception.CustomException;
import com.coffeecode.processor.config.CSVConfig;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvRuntimeException;
import com.opencsv.exceptions.CsvValidationException;

public class CSVProcessor extends AbstractFileProcessor {
    private static final String CSV_EXTENSION = ".csv";
    private static final String ERROR_INIT = "CSV_INIT_ERROR";
    private static final String ERROR_READ = "CSV_READ_ERROR";
    private static final String ERROR_CLOSE = "CSV_CLOSE_ERROR";

    private CSVReader csvReader;
    private final CSVConfig csvConfig;

    public CSVProcessor(CSVConfig config) {
        super(config);
        if (config == null) {
            throw new IllegalArgumentException("CSV configuration cannot be null");
        }
        this.csvConfig = config;
    }

    @Override
    protected boolean isValidExtension(File file) {
        return file.getName().toLowerCase().endsWith(CSV_EXTENSION);
    }

    @Override
    protected void initializeReader() throws CustomException {
        try {
            csvReader = new CSVReaderBuilder(
                    Files.newBufferedReader(file.toPath(), Charset.forName(csvConfig.getCharset())))
                    .withSkipLines(csvConfig.getSkipLines())
                    .withCSVParser(new CSVParserBuilder()
                            .withSeparator(csvConfig.getSeparator())
                            .withQuoteChar(csvConfig.getQuoteChar())
                            .build())
                    .build();
        } catch (IOException e) {
            throw new CustomException("Failed to initialize CSV reader", ERROR_INIT, e);
        }
    }

    @Override
    public Iterator<List<String>> getIterator() throws CustomException {
        if (csvReader == null) {
            initializeReader();
        }

        return new Iterator<List<String>>() {
            private String[] nextLine;

            @Override
            public boolean hasNext() {
                try {
                    nextLine = csvReader.readNext();
                    return nextLine != null;
                } catch (CsvValidationException | IOException e) {
                    logger.error("Failed to read CSV line", e);
                    throw new CsvRuntimeException("Failed to read CSV line", e);
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
    }

    @Override
    protected void closeReader() throws CustomException {
        try {
            if (csvReader != null) {
                csvReader.close();
            }
        } catch (IOException e) {
            throw new CustomException("Failed to close CSV reader", ERROR_CLOSE, e);
        }
    }

    @Override
    public FileType getFileType() {
        return FileType.CSV;
    }
}