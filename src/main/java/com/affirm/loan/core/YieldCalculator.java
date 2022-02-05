package com.affirm.loan.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class YieldCalculator {

    public double calculate(Facility facility, Loan loan) {
        return (1 - loan.getDefaultLikelyHood()) * loan.getInterestRate() * loan.getAmount() - loan.getDefaultLikelyHood() * loan.getAmount() - facility.getInterestRate() * loan.getAmount();
    }
}
