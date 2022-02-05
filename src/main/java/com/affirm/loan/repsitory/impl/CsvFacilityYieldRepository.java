package com.affirm.loan.repsitory.impl;

import com.affirm.loan.core.Facility;
import com.affirm.loan.repsitory.FacilityYieldRepository;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvFacilityYieldRepository implements FacilityYieldRepository {

    File facilityYieldStore;

    public CsvFacilityYieldRepository(File facilityYieldStore) {
        this.facilityYieldStore = facilityYieldStore;
    }

    @Override
    public void addAll(List<Facility> facilityList) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(facilityYieldStore))) {
            csvWriter.writeNext(new String[]{"facility_id", "expected_yield"});
            facilityList.stream().forEach(facility -> {
                csvWriter.writeNext(new String[]{facility.getId() + "", ((int) facility.getCalculatedYield()) + ""});
            });
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while writing yields report " + e.getMessage(), e);
        }
    }
}
