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
        if (facilityCovenantMap.get(facility.getId()) != null) {
            return facilityCovenantMap.get(facility.getId()).stream().filter(c -> c.canFund(loan, facility)).count() > 0;
        } else if (bankCovenantMap.get(facility.getBank().getBankId()) != null) {
            return bankCovenantMap.get(facility.getId()).stream().filter(c -> c.canFund(loan, facility)).count() > 0;
        }
        return false;
    }
}
