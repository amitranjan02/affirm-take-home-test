package com.affirm.loan.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanAssignmentEvent {

    private boolean loanAssigned;
    private Loan loan;
    private Facility facility;

    public static LoanAssignmentEvent noLoanAssigned() {
        return LoanAssignmentEvent.builder().loanAssigned(false).build();
    }
}
