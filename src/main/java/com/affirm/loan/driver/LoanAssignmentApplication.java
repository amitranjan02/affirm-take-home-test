package com.affirm.loan.handler;

import com.affirm.loan.core.LoanAssignment;
import com.affirm.loan.core.LoanAssignmentHandler;
import com.affirm.loan.repsitory.CsvBankAndFacilityRepository;
import com.affirm.loan.repsitory.CsvLoanRepository;
import com.affirm.loan.repsitory.LoanRepository;

import java.io.File;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class LoanAssignmentApplication {
    public static void main(String[] args) {

        CsvBankAndFacilityRepository csvBankAndFacilityRepository = new CsvBankAndFacilityRepository(new File("data/small/facilities.csv"), new File("data/small/banks.csv"), new File("data/small/covenants.csv"));
        LoanRepository loanRepository = new CsvLoanRepository(new File("data/small/loans.csv"));

        LoanAssignmentHandler loanAssignmentHandler = new LoanAssignmentHandler(csvBankAndFacilityRepository);
        List<LoanAssignment> loanAssignmentList = loanRepository.getAllLoans().stream().map(loan -> loanAssignmentHandler.assignLoan(loan)).collect(toList());
        Map<Integer, Integer> yieldByFacilityId = loanAssignmentHandler.getYieldByFacilityId();

    }
}
