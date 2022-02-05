package com.affirm.loan.repsitory;

import com.affirm.loan.core.Loan;

import java.util.List;

public interface LoanRepository {

    List<Loan> getAllLoans();
}
