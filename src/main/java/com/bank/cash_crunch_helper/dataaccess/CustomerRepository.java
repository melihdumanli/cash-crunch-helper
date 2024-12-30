package com.bank.cash_crunch_helper.dataaccess;

import com.bank.cash_crunch_helper.dbmodel.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByIdentityNumber(Long nationalId);
}
