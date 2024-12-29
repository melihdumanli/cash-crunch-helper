package com.bank.cash_crunch_helper.config.data;

import com.bank.cash_crunch_helper.constant.Role;
import com.bank.cash_crunch_helper.dataaccess.CustomerRepository;
import com.bank.cash_crunch_helper.dataaccess.LoanInstallmentRepository;
import com.bank.cash_crunch_helper.dataaccess.LoanRepository;
import com.bank.cash_crunch_helper.dataaccess.UserRepository;
import com.bank.cash_crunch_helper.dbmodel.Customer;
import com.bank.cash_crunch_helper.dbmodel.Loan;
import com.bank.cash_crunch_helper.dbmodel.LoanInstallment;
import com.bank.cash_crunch_helper.dbmodel.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor
class DataInitializer {
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(CustomerRepository customerRepository, LoanRepository loanRepository, LoanInstallmentRepository installmentRepository) {
        return args -> {
            Customer customer = new Customer();
            customer.setName("John");
            customer.setSurname("Doe");
            customer.setCreditLimit(10000.0);
            customer.setUsedCreditLimit(0.0);
            customer.setLoans(new ArrayList<>());

            Loan loan = new Loan();
            loan.setCustomer(customer);
            loan.setLoanAmount(6000.0);
            loan.setNumberOfInstallments(12);
            loan.setCreateDate(LocalDate.now());
            loan.setIsPaid(false);
            loan.setInstallments(new ArrayList<>(12));

            for (int i = 1; i <= 12; i++) {
                LoanInstallment installment = new LoanInstallment();
                installment.setLoan(loan);
                installment.setAmount(6000.0 / 12);
                installment.setPaidAmount(0.0);
                installment.setDueDate(LocalDate.now().plusMonths(i));
                installment.setPaymentDate(null);
                installment.setIsPaid(false);
                loan.getInstallments().add(installment);
            }

            customer.getLoans().add(loan);
            customerRepository.save(customer);
        };
    }

    @Bean
    CommandLineRunner initUser(UserRepository userRepository) {
        return args -> {
            User admin = User.builder()
                            .firstname("Ahmet")
                            .lastname("Yılmaz")
                            .email("ahmetyilmaz@example.com")
                            .password(passwordEncoder.encode("ayilmaz"))
                            .role(Role.ADMIN)
                            .build();
            User user = User.builder()
                            .firstname("Mehmet")
                            .lastname("Korkmaz")
                            .email("mehmetkorkmaz@example.com")
                            .password(passwordEncoder.encode("mkorkmaz"))
                            .role(Role.USER)
                            .build();
            userRepository.save(admin);
            userRepository.save(user);
        };
    }
}