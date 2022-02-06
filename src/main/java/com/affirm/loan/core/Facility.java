package com.affirm.loan.core;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Facility {

    int id;
    double interestRate;
    long amountInCents;
    double calculatedYield;

    Bank bank;
    Set<Covenant> covenantSet;

    public boolean canFund(Loan loan) {
        return (amountInCents >= loan.getAmount()) && CompositeCovenant.canFund(loan, this, covenantSet);
    }

    public void fund(Loan loan) {
        this.amountInCents = this.amountInCents - loan.getAmount();
    }
}
