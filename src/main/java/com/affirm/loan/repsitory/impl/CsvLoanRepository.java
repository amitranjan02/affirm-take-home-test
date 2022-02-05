package com.affirm.loan.repsitory.impl;

import com.affirm.loan.core.Loan;
import com.affirm.loan.repsitory.LoanRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CsvLoanRepository implements LoanRepository {

    private List<Loan> loans;

    public CsvLoanRepository(File csfFile) {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csfFile)).withSkipLines(1).build()) {
            loans = csvReader.readAll().stream().map(this::mapToLoan).collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read supplied file to load LoanRepository " + e.getMessage(), e);
        } catch (CsvException e) {
            throw new IllegalArgumentException("Could not read supplied file to load LoanRepository " + e.getMessage(), e);
        }
    }

    private Loan mapToLoan(String[] line) {
        return Loan.builder()
                .interestRate(Float.parseFloat(line[0]))
                .amount(Integer.parseInt(line[1]))
                .id(Integer.parseInt(line[2]))
                .defaultLikelyHood(Float.parseFloat(line[3]))
                .state(line[4])
                .build();
    }

    @Override
    public List<Loan> getAllLoans() {
        return loans;
    }
}
