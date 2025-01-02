package com.coffeecode.processor;

import java.io.BufferedReader;
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
            BufferedReader reader = Files.newBufferedReader(file.toPath(), Charset.forName(csvConfig.getCharset()));
            reader.mark(1);
            int firstChar = reader.read();
            if (firstChar != '\ufeff' && firstChar != -1) {
                reader.reset();
            } else if (firstChar == -1) {
                throw new CustomException("Empty CSV file", ERROR_INIT);
            }
            csvReader = new CSVReaderBuilder(reader)
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
            private boolean hasNextCalled = false;

            @Override
            public boolean hasNext() {
                if (!hasNextCalled) {
                    readNextLine();
                }
                return nextLine != null;
            }

            @Override
            public List<String> next() {
                if (!hasNextCalled && !hasNext()) {
                    throw new NoSuchElementException("No more elements in the CSV file");
                }
                if (nextLine == null) {
                    throw new NoSuchElementException("No more elements in the CSV file");
                }
                hasNextCalled = false;
                return convertLineToList(nextLine);
            }

            private void readNextLine() {
                try {
                    nextLine = csvReader.readNext();
                    hasNextCalled = true;
                } catch (CsvValidationException | IOException e) {
                    logger.error("Failed to read CSV line", e);
                    throw new CsvRuntimeException("Failed to read CSV line", e);
                }
            }

            private List<String> convertLineToList(String[] line) {
                List<String> lineAsList = new ArrayList<>();
                for (String element : line) {
                    lineAsList.add(element != null ? element : "");
                }
                return lineAsList;
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