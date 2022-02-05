package com.affirm.loan.repsitory.impl;

import com.affirm.loan.core.Bank;
import com.affirm.loan.core.Covenant;
import com.affirm.loan.core.Facility;
import com.affirm.loan.repsitory.BankAndFacilityRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class CsvBankAndFacilityRepository implements BankAndFacilityRepository {

    List<Facility> facilityList;
    Map<Integer, Bank> bankMap;
    Set<Covenant> covenantSet;

    public CsvBankAndFacilityRepository(File facilitiesFile, File bankFile, File covenantsFile) {
        bankMap = loadAndMap(bankFile, this::mapToBank, toMap(Bank::getBankId, bank -> bank));
        covenantSet = new CsvCovenantRepository(covenantsFile).getAllCovenants();
        facilityList = loadAndMap(facilitiesFile, this::mapToFacility, Collectors.toList());
        facilityList.sort(Comparator.comparingDouble(Facility::getInterestRate));
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
                .bank(bankMap.get(Integer.parseInt(lines[3].trim()))) //TODO bankMap and covenantSet dont need to be in member variables - they are useless beyond this point.
                .covenantSet(covenantSet)
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
