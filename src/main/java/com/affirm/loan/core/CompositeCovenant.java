package com.affirm.loan.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
public class CompositeCovenant implements Covenant {

    public enum CovenantLevel {
        BANK, FACILITY;
    }

    Map<Integer, Set<Covenant>> bankCovenantMap;
    Map<Integer, Set<Covenant>> facilityCovenantMap;

    public CompositeCovenant() {
        this.bankCovenantMap = new HashMap<>();
        this.facilityCovenantMap = new HashMap<>();
    }

    public void add(Integer id, Covenant covenant, CovenantLevel covenantLevel) {
        switch (covenantLevel) {
            case BANK:
                merge(id, covenant, bankCovenantMap);
            case FACILITY:
                merge(id, covenant, facilityCovenantMap);
        }
    }

    private void merge(int id, Covenant covenant, Map<Integer, Set<Covenant>> map) {
        map.compute(id, (k, v) -> {
            if (v == null) {
                v = new HashSet<>();
            }
            v.add(covenant);
            return v;
        });
    }

    @Override
    public boolean canFund(Loan loan, Facility facility) {
        Set<Covenant> facilityCovenantSet = facilityCovenantMap.get(facility.getId());
        Set<Covenant> bankCovenantSet = bankCovenantMap.get(facility.getBank().getBankId());
        return canFund(loan, facility, facilityCovenantSet) && canFund(loan, facility, bankCovenantSet);
    }

    public static boolean canFund(Loan loan, Facility facility, Set<Covenant> covenantSet) {
        if (covenantSet == null) {
            return true;
        }
        for (Covenant covenant : covenantSet) {
            if (!covenant.canFund(loan, facility)) {
                return false;
            }
        }
        return true;
    }
}
