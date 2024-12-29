package com.bank.cash_crunch_helper.dataaccess;

import com.bank.cash_crunch_helper.dbmodel.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
