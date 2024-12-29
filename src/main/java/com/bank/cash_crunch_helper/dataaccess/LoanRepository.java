package com.bank.cash_crunch_helper.dataaccess;

import com.bank.cash_crunch_helper.dbmodel.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
