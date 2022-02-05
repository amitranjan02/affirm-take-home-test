package com.affirm.loan.core;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Facility {

    int id;
    double interestRate;
    double amountInCents;
    double calculatedYield;

    Bank bank;
    Set<Covenant> covenantSet;

    @Builder.Default
    YieldCalculator yieldCalculator = new YieldCalculator();

    public boolean canFund(Loan loan) {
        boolean canFund = amountInCents >= loan.getAmount() && CompositeCovenant.canFund(loan, this, covenantSet);
        return canFund;
    }

    public void fund(Loan loan) {
        this.amountInCents = this.amountInCents - loan.getAmount();
        this.calculatedYield += yieldCalculator.calculate(this, loan);
    }
}
