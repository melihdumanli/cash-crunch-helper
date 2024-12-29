package com.bank.cash_crunch_helper.config.data;

import com.bank.cash_crunch_helper.dataaccess.CustomerRepository;
import com.bank.cash_crunch_helper.dataaccess.LoanInstallmentRepository;
import com.bank.cash_crunch_helper.dataaccess.LoanRepository;
import com.bank.cash_crunch_helper.dbmodel.Customer;
import com.bank.cash_crunch_helper.dbmodel.Loan;
import com.bank.cash_crunch_helper.dbmodel.LoanInstallment;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;

@Configuration
class DataInitializer {

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
}