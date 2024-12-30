package com.bank.cash_crunch_helper.dataaccess;

import com.bank.cash_crunch_helper.dbmodel.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long>{

    @Query("SELECT i FROM LoanInstallment i WHERE i.loan.id = ?1")
    List<LoanInstallment> findInstallmentsByLoanId(Long loanId);
}
