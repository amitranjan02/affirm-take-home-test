package com.affirm.loan.core;

import com.affirm.loan.repsitory.BankAndFacilityRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class LoanAssignmentHandler {

    BankAndFacilityRepository facilitiesRepository;
    LoanAssignmentListener loanAssignmentListener;

    public LoanAssignmentEvent assignLoan(Loan loan) {
        List<Facility> facilities = facilitiesRepository.getAllFacility();
        return facilities.stream().filter(f -> f.canFund(loan)).findFirst().map(f -> {
            f.fund(loan);
            LoanAssignmentEvent loanAssignment = LoanAssignmentEvent.builder().loanAssigned(true).loan(loan).facility(f).build();
            loanAssignmentListener.onLoanAssigned(loanAssignment);
            return loanAssignment;
        }).orElse(LoanAssignmentEvent.noLoanAssigned());
    }
}
