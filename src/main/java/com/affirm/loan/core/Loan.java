package com.affirm.loan.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Loan {
    int id;
    int amount;
    float interestRate;
    float defaultLikelyHood;
    String state;

}
