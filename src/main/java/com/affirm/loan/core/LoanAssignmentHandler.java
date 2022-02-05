package com.affirm.loan.core;

import com.affirm.loan.repsitory.BankAndFacilityRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class LoanAssignmentHandler {

    BankAndFacilityRepository facilitiesRepository;

    public LoanAssignment assignLoan(Loan loan) {
        List<Facility> facilities = facilitiesRepository.getAllFacility();
        Optional<Facility> facility = facilities.stream().filter(f -> f.canFund(loan)).findFirst();
        return facility.map(f -> {
            f.fund(loan);
            return LoanAssignment.builder().loanAssigned(true).loan(loan).facility(f).build();
        }).orElse(LoanAssignment.noLoanAssigned());
    }

    public Map<Integer, Integer> getYieldByFacilityId() {
        List<Facility> facilities = facilitiesRepository.getAllFacility();
        return facilities.stream().collect(Collectors.toMap(e -> e.getId(), e -> (int) e.getCalculatedYield()));
    }
}
