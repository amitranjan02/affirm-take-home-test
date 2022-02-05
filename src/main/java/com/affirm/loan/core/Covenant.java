package com.affirm.loan.core;

public interface Covenant {

    public boolean canFund(Loan loan, Facility facility);

    public static Covenant noOpCovenant() {
        return (loan, facility) -> true;
    }
}
