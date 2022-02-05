package com.affirm.loan.repsitory;

import com.affirm.loan.core.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class CsvBankAndFacilityRepository implements BankAndFacilityRepository {

    List<Facility> facilityList;
    Map<Integer, Bank> bankMap;
    CompositeCovenant compositeCovenant;

    public CsvBankAndFacilityRepository(File facilitiesFile, File bankFile, File covenantsFile) {
        bankMap = loadAndMap(bankFile, this::mapToBank, toMap(Bank::getBankId, bank -> bank));
        compositeCovenant = createCompositeCovenant(covenantsFile);
        facilityList = loadAndMap(facilitiesFile, this::mapToFacility, Collectors.toList());
        facilityList.sort(Comparator.comparingDouble(Facility::getInterestRate));
    }

    private CompositeCovenant createCompositeCovenant(File covenantsFile) {

        CompositeCovenant compositeCovenant = new CompositeCovenant();
        //TODO this is duplicate code.
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(covenantsFile)).withSkipLines(1).build()) {
            csvReader.readAll().stream().forEach(line -> {

                //Create Covant
                Covenant delegateCovenant = getDelegateCovenant(line);

                if (!StringUtils.isEmpty(line[0])) {
                    compositeCovenant.add(Integer.parseInt(line[0]), delegateCovenant, CompositeCovenant.CovenantLevel.FACILITY);
                }
                if (!StringUtils.isEmpty(line[2])) {
                    compositeCovenant.add(Integer.parseInt(line[2]), delegateCovenant, CompositeCovenant.CovenantLevel.BANK);
                }
            });
            return compositeCovenant;
        } catch (IOException | CsvException e) {
            throw new IllegalArgumentException("Could not read supplied file to load LoanRepository " + e.getMessage(), e);
        }
    }

    private <T> Covenant mapToCovenant(String[] strings) {

        CompositeCovenant.CompositeCovenantBuilder compositeCovenantBuilder = CompositeCovenant.builder();
        return getDelegateCovenant(strings);
    }

    private Covenant getDelegateCovenant(String[] strings) {
        if (StringUtils.isNotEmpty(strings[1])) {
            return LoanDefaultBasedCovenant.builder().maxDefaultLikelyHood(Float.parseFloat(strings[1])).build();
        } else if (StringUtils.isNotEmpty(strings[3])) {
            return LocationBasedCovenant.builder().bannedLocations(Arrays.asList(strings[3])).build();
        } else {
            return Covenant.noOpCovenant();
        }
    }


    private <T> Bank mapToBank(String[] strings) {
        return Bank.builder().bankId(Integer.parseInt(strings[0])).bankName(strings[1]).build();
    }

    private Facility mapToFacility(String[] lines) {
        return Facility
                .builder()
                .amountInCents(Double.parseDouble(lines[0]))
                .interestRate(Double.parseDouble(lines[1]))
                .id(Integer.parseInt(lines[2]))
                .bank(bankMap.get(Integer.parseInt(lines[2])))
                .covenantSet(Set.of(compositeCovenant))
                .build();
    }

    private <T, Y> Y loadAndMap(File facilitiesFile, Function<String[], T> mapper, Collector<T, ?, Y> collector) {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(facilitiesFile)).withSkipLines(1).build()) {
            return csvReader.readAll().stream().map(mapper).collect(collector);
        } catch (IOException | CsvException e) {
            throw new IllegalArgumentException("Could not read supplied file to load LoanRepository " + e.getMessage(), e);
        }
    }

    @Override
    public List<Facility> getAllFacility() {
        return facilityList;
    }
}
