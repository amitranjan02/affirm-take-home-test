package com.affirm.loan.driver;

import com.affirm.loan.core.LoanAssignmentHandler;
import com.affirm.loan.core.CsvExportableLoanAssignmentListener;
import com.affirm.loan.repsitory.BankAndFacilityRepository;
import com.affirm.loan.repsitory.LoanRepository;
import com.affirm.loan.repsitory.impl.CsvBankAndFacilityRepository;
import com.affirm.loan.repsitory.impl.CsvLoanRepository;

import java.io.File;

public class LoanAssignmentApplication {

    private static BankAndFacilityRepository facilityRepository;
    private static LoanRepository loanRepository;
    private static CsvExportableLoanAssignmentListener loanAssignmentListener;

    public static void main(String[] args) {

        String baseFolder = new File(args.length == 1 ? args[0] : "data/small").getAbsolutePath();
        initComponents(baseFolder);

        System.out.println("Starting loan assignments");
        LoanAssignmentHandler loanAssignmentHandler = new LoanAssignmentHandler(facilityRepository, loanAssignmentListener);
        long count =  loanRepository.getAllLoans().stream().map(loanAssignmentHandler::assignLoan).count();
        System.out.println("Loan assignment completed for "+count+" loans");

        System.out.println("Generating loan assignment and yields results");
        loanAssignmentListener.generateReport();
        System.out.println("Generated loan assignment and yields results can be found in " + baseFolder + " folder");

    }

    private static void initComponents(String baseFolder) {
        System.out.println("Loading files from " + baseFolder + " folder");
        loanAssignmentListener = new CsvExportableLoanAssignmentListener(new File(baseFolder));
        facilityRepository = new CsvBankAndFacilityRepository(new File(baseFolder + "/facilities.csv"),
                new File(baseFolder + "/banks.csv"), new File(baseFolder + "/covenants.csv"));
        loanRepository = new CsvLoanRepository(new File(baseFolder + "/loans.csv"));
        System.out.println("Loading complete");
    }
}
