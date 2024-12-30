package com.bank.cash_crunch_helper.dataaccess;

import com.bank.cash_crunch_helper.dbmodel.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("select l from Loan l where l.customer.id = :customerId and l.isPaid = false")
    List<Loan> findUnpaidLoansByCustomerId(Long customerId);
}
