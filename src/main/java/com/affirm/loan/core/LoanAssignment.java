package com.affirm.loan.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanAssignment {

    private boolean loanAssigned;
    private Loan loan;
    private Facility facility;

    public static LoanAssignment noLoanAssigned() {
        return LoanAssignment.builder().loanAssigned(false).build();
    }
}
