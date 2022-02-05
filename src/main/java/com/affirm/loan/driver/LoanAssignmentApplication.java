package com.affirm.loan.driver;

import com.affirm.loan.core.LoanAssignment;
import com.affirm.loan.core.LoanAssignmentHandler;
import com.affirm.loan.repsitory.BankAndFacilityRepository;
import com.affirm.loan.repsitory.FacilityYieldRepository;
import com.affirm.loan.repsitory.LoanAssignmentRepository;
import com.affirm.loan.repsitory.impl.CsvBankAndFacilityRepository;
import com.affirm.loan.repsitory.impl.CsvFacilityYieldRepository;
import com.affirm.loan.repsitory.impl.CsvLoanAssignmentRepository;
import com.affirm.loan.repsitory.impl.CsvLoanRepository;
import com.affirm.loan.repsitory.LoanRepository;

import java.io.File;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class LoanAssignmentApplication {

    private static BankAndFacilityRepository facilityRepository;
    private static LoanRepository loanRepository;
    private static FacilityYieldRepository facilityYieldRepository;
    private static LoanAssignmentRepository loanAssignmentRepository;

    public static void main(String[] args) {

        String baseFolder = args.length == 1 ? args[0] : "data/small";
        initRepositories(baseFolder);

        LoanAssignmentHandler loanAssignmentHandler = new LoanAssignmentHandler(facilityRepository);
        List<LoanAssignment> loanAssignmentList = loanRepository.getAllLoans().stream().map(loan -> loanAssignmentHandler.assignLoan(loan)).collect(toList());

        facilityYieldRepository.addAll(facilityRepository.getAllFacility());
        loanAssignmentRepository.addAll(loanAssignmentList);

    }

    private static void initRepositories(String baseFolder) {
        facilityRepository = new CsvBankAndFacilityRepository(new File(baseFolder + "/facilities.csv"),
                new File(baseFolder + "/banks.csv"), new File(baseFolder + "/covenants.csv"));
        loanRepository = new CsvLoanRepository(new File(baseFolder + "/loans.csv"));
        facilityYieldRepository = new CsvFacilityYieldRepository(new File(baseFolder + "/yields.csv"));
        loanAssignmentRepository = new CsvLoanAssignmentRepository(new File(baseFolder + "/assignments.csv"));
    }
}
