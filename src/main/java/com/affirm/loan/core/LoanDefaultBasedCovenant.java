package com.affirm.loan.core;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoanDefaultBasedCovenant implements Covenant {

    private float maxDefaultLikelyHood;

    @Override
    public boolean canFund(Loan loan, Facility facility) {

        return loan.getDefaultLikelyHood() < maxDefaultLikelyHood;
    }
}
