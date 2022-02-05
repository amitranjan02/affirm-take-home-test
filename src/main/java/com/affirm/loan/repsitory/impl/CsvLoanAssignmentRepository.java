package com.affirm.loan.repsitory.impl;

import com.affirm.loan.core.LoanAssignment;
import com.affirm.loan.repsitory.LoanAssignmentRepository;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvLoanAssignmentRepository implements LoanAssignmentRepository {

    File loanAssignmentFile;

    public CsvLoanAssignmentRepository(File loanAssignmentFile) {
        this.loanAssignmentFile = loanAssignmentFile;
    }

    @Override
    public void addAll(List<LoanAssignment> loanAssignmentList) {

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(loanAssignmentFile))) {
            csvWriter.writeNext(new String[]{"loan_id", "facility_id"});
            loanAssignmentList.stream().filter(loanAssignment -> loanAssignment.isLoanAssigned()).forEach(loanAssignment -> {
                csvWriter.writeNext(new String[]{loanAssignment.getLoan().getId() + "", loanAssignment.getFacility().getId() + ""});
            });
            csvWriter.flush();

        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while writing facilityAssignment report " + e.getMessage(), e);

        }

    }
}
