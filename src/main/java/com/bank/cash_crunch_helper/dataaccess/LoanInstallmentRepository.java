package com.bank.cash_crunch_helper.dataaccess;

import com.bank.cash_crunch_helper.dbmodel.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long>{
}
