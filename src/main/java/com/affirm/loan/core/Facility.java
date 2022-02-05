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
    int calculatedYield;

    Bank bank;
    Set<Covenant> covenantSet;

    @Builder.Default
    YieldCalculator yieldCalculator = new YieldCalculator();

    public boolean canFund(Loan loan) {
        return covenantSet.stream().filter(covenant -> covenant.canFund(loan, this)).count() == covenantSet.size();
    }

    public void fund(Loan loan) {
        this.amountInCents = this.amountInCents - loan.getAmount();
        this.calculatedYield += yieldCalculator.calculate(this, loan);
    }

}
