package com.affirm.loan.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDefaultRateAndLocationBasedCovenant implements Covenant {

    private float maxDefaultLikelyHood;

    @Builder.Default
    private Set<String> bannedStates = new HashSet<>();

    @Override
    public boolean canFund(Loan loan, Facility facility) {

        return loan.getDefaultLikelyHood() <= maxDefaultLikelyHood && !bannedStates.contains(loan.getState());
    }

    public void addToBannedStates(String state) {
        bannedStates.add(state);
    }
}
