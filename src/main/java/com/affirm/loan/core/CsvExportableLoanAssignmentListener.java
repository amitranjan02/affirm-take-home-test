package com.affirm.loan.core;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CsvExportableLoanAssignmentListener implements LoanAssignmentListener{

    File reportDirectory;
    YieldCalculator yieldCalculator;
    List<LoanAssignmentEvent> loanAssignmentList;
    Map<Integer, Integer> facilityYieldMap;

    public CsvExportableLoanAssignmentListener(File reportDirectory) {
        this.yieldCalculator = new YieldCalculator();
        this.reportDirectory = reportDirectory;
        this.loanAssignmentList = new ArrayList<>();
        this.facilityYieldMap = new HashMap<>();
    }

    @Override
    public void onLoanAssigned(LoanAssignmentEvent loanAssignment) {
        loanAssignmentList.add(loanAssignment);
        facilityYieldMap.compute(loanAssignment.getFacility().getId(), (k, v) -> {
            double yield = yieldCalculator.calculate(loanAssignment.getFacility(), loanAssignment.getLoan());
            return v == null ? (int) yield : (int) (v + yield);
        });
    }

    public void generateReport() {
        generateLoanAssignmentsReport();
        generateYieldReport();
    }

    private void generateYieldReport() {
        generateReport(new File(reportDirectory.getAbsolutePath() + "/yield.csv"), csvWriteer -> {
            csvWriteer.writeNext(new String[]{"facility_id", "expected_yield"});
            facilityYieldMap.forEach((key, value) -> csvWriteer.writeNext(new String[]{key + "", value + ""}));
        });
    }

    private void generateLoanAssignmentsReport() {
        generateReport(new File(reportDirectory.getAbsolutePath() + "/assignments.csv"), csvWriteer -> {
            csvWriteer.writeNext(new String[]{"loan_id", "facility_id"});
            loanAssignmentList.forEach(la -> csvWriteer.writeNext(new String[]{la.getLoan().getId() + "", la.getFacility().getId() + ""}));
        });
    }

    public void generateReport(File outputFile, Consumer<CSVWriter> writerConsumer) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFile))) {
            writerConsumer.accept(csvWriter);
            csvWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while generating report " + e.getMessage(), e);
        }
    }
}
