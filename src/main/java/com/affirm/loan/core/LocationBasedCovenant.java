package com.affirm.loan.core;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class LocationBasedCovenant implements Covenant {

    List<String> bannedLocations;

    @Override
    public boolean canFund(Loan loan, Facility facility) {
        return !bannedLocations.contains(loan.getState());
    }
}
