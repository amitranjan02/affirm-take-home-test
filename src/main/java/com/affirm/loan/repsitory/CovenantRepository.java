package com.affirm.loan.repsitory;

import com.affirm.loan.core.Covenant;

import java.util.Set;

public interface CovenantRepository {
    Set<Covenant> getAllCovenants();
}
