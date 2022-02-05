package com.affirm.loan.repsitory;

import com.affirm.loan.core.Facility;

import java.util.List;

public interface FacilityYieldRepository {
    public void addAll(List<Facility> facilityList);
}
