package com.affirm.loan.repsitory;

import com.affirm.loan.core.CompositeCovenant;
import com.affirm.loan.core.Covenant;
import com.affirm.loan.core.LoanDefaultRateAndLocationBasedCovenant;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvCovenantRepository {

    File covenantsFile;

    CsvCovenantRepository(File file) {
        this.covenantsFile = file;
    }

    Set<Covenant> getAllCovenants() {

        Map<String, List<String[]>> facilityBaseCovenantMap = new HashMap<>();
        Map<String, List<String[]>> bankBasedCovenantMap = new HashMap<>();
        CompositeCovenant compositeCovenant = new CompositeCovenant();

        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(covenantsFile)).withSkipLines(1).build()) {
            csvReader.readAll().stream().forEach(line -> {
                if (StringUtils.isNotEmpty(line[0])) {
                    addToMap(facilityBaseCovenantMap, line[0], line);
                } else {
                    addToMap(bankBasedCovenantMap, line[0], line);
                }
            });
            facilityBaseCovenantMap.entrySet().forEach(e -> compositeCovenant.add(Integer.parseInt(e.getKey()), getDelegateCovenant(e.getValue()), CompositeCovenant.CovenantLevel.FACILITY));
            bankBasedCovenantMap.entrySet().forEach(e -> compositeCovenant.add(Integer.parseInt(e.getKey()), getDelegateCovenant(e.getValue()), CompositeCovenant.CovenantLevel.BANK));
            HashSet<Covenant> covenantSet = new HashSet<>();
            covenantSet.add(compositeCovenant);
            return covenantSet;
        } catch (IOException | CsvException e) {
            throw new IllegalArgumentException("Could not read supplied file to load LoanRepository " + e.getMessage(), e);
        }
    }

    private void addToMap(Map<String, List<String[]>> facilityBaseCovenantMap, String key, String[] line) {
        facilityBaseCovenantMap.compute(key, (k, v) -> {
            if (v == null) {
                v = new ArrayList<>();
            }
            v.add(line);
            return v;
        });
    }

    private LoanDefaultRateAndLocationBasedCovenant getDelegateCovenant(List<String[]> strings) {
        LoanDefaultRateAndLocationBasedCovenant covenant = new LoanDefaultRateAndLocationBasedCovenant();
        strings.stream().forEach(arr -> {
            String maxDefault = arr[1];
            String bannedState = arr[3];
            if (StringUtils.isNotEmpty(maxDefault)) {
                covenant.setMaxDefaultLikelyHood(Float.parseFloat(maxDefault));
            }
            if (StringUtils.isNotEmpty(bannedState)) {
                covenant.addToBannedStates(bannedState);
            }
        });
        return covenant;
    }
}
