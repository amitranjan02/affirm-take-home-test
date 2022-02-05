package com.affirm.loan.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class YieldCalculator {

    public int calculate(Facility facility, Loan loan) {
        return 0;
    }
}
