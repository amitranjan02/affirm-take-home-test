package com.affirm.loan.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Bank {
    private int bankId;
    private String bankName;
}
