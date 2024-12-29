package com.bank.cash_crunch_helper.dataaccess;

import com.bank.cash_crunch_helper.dbmodel.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
