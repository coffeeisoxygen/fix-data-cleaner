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
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

public class CSVProcessor implements FileProcessor {
    private File file;
    private CSVReader csvReader;

    @Override
    public boolean validateFile(File file) {
        this.file = file;
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".csv");
    }

    @Override
    public Iterator<List<String>> getIterator() throws CustomException {
        try {
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
            throw new CustomException("Error reading CSV file", e);
        }
    }

    @Override
    public List<List<String>> readToArray() {
        List<List<String>> data = new ArrayList<>();
        try {
            Iterator<List<String>> iterator = getIterator();
            while (iterator.hasNext()) {
                data.add(iterator.next());
            }
        } catch (CustomException e){ 
            System.out.println(e.getMessage());
        }
        return data;
    }
}