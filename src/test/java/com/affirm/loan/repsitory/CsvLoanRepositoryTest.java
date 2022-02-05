package com.affirm.loan.repsitory;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class CsvLoanRepositoryTest {

    CsvLoanRepository subject;

    @Test
    public void itShouldLoadAllLoans() {
        subject = new CsvLoanRepository(new File("data/small/loans.csv"));
        assertThat(subject.getAllLoans()).hasSize(3);
    }

}