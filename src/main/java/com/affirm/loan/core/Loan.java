package com.affirm.loan.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Loan {
    int id;
    long amount;
    float interestRate;
    float defaultLikelyHood;
    String state;

}
