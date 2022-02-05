package com.affirm.loan.repsitory;

import com.affirm.loan.repsitory.impl.CsvBankAndFacilityRepository;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class CsvBankAndFacilityRepositoryTest {

    @Test
    public void itShouldLoadFacility() {
        CsvBankAndFacilityRepository csvBankAndFacilityRepository = new CsvBankAndFacilityRepository(new File("data/small/facilities.csv"), new File("data/small/banks.csv"), new File("data/small/covenants.csv"));
        assertThat(csvBankAndFacilityRepository.getAllFacility()).hasSize(2);
    }

}