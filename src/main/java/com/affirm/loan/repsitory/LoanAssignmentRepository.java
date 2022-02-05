package com.affirm.loan.repsitory;

import com.affirm.loan.core.LoanAssignment;

import java.util.List;

public interface LoanAssignmentRepository {

    public void addAll(List<LoanAssignment> loanAssignmentList);
}
