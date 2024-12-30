package com.bank.cash_crunch_helper.service;

import com.bank.cash_crunch_helper.dataaccess.CustomerRepository;
import com.bank.cash_crunch_helper.dbmodel.Customer;
import com.bank.cash_crunch_helper.dbmodel.Loan;
import com.bank.cash_crunch_helper.dbmodel.LoanInstallment;
import com.bank.cash_crunch_helper.dto.request.CustomerRequestDTO;
import com.bank.cash_crunch_helper.dto.response.CustomerResponseDTO;
import com.bank.cash_crunch_helper.exception.BusinessException;
import com.bank.cash_crunch_helper.exception.ExceptionSeverity;
import com.bank.cash_crunch_helper.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private static final String CUSTOMER_NOT_FOUND = "Customer not found";
    private static final String RISKY_CUSTOMER = "Risky customer. Credit limit cannot be calculated.";
    private static final String CREDIT_LIMIT_OUT_OF_RANGE = "Credit limit is out of range. Must be between 0 and 1900";
    private final CustomerRepository customerRepository;

    private Double calculateCreditLimit(Double netIncome, Integer creditScore) {
        double creditLimit = 0;

        if (creditScore >= 0 && creditScore <= 699) {
           throw new BusinessException(ExceptionSeverity.WARNING, RISKY_CUSTOMER);
        } else if (creditScore >= 700 && creditScore <= 1099) {
            creditLimit = netIncome * 0.40;
        } else if (creditScore >= 1100 && creditScore <= 1499) {
            creditLimit = netIncome * 1.60;
        } else if (creditScore >= 1500 && creditScore <= 1699) {
            creditLimit = netIncome * 3.80;
        } else if (creditScore >= 1700 && creditScore <= 1900) {
            creditLimit = netIncome * 5.00;
        } else {
            throw new BusinessException(ExceptionSeverity.ERROR, CREDIT_LIMIT_OUT_OF_RANGE);
        }

        return creditLimit;
    }

    private Boolean isLoanTermValid(Integer requestedLoanTerm) {
        List<Integer> validLoanTerms = List.of(6, 9, 12, 24);
        return validLoanTerms.contains(requestedLoanTerm);
    }

    private Boolean isInterestRateValid(Double interestRate) {
        return interestRate >= 0.1 && interestRate <= 0.5;
    }

    public CustomerResponseDTO findCustomerByNationalId(Long nationalId) {
        Optional<Customer> optionalCustomer = customerRepository.findByIdentityNumber(nationalId);
        if (optionalCustomer.isEmpty()) {
            throw new BusinessException(ExceptionSeverity.ERROR, CUSTOMER_NOT_FOUND);
        }
        return CustomerMapper.INSTANCE.convertToDTO(optionalCustomer.get());
    }

    private Boolean isValidRequest(CustomerRequestDTO customerRequestDTO) {
        return customerRequestDTO.getNetIncome() != null && customerRequestDTO.getCreditScore() != null && customerRequestDTO.getRequestedLoanAmount() != null && customerRequestDTO.getRequestedLoanTerm() != null && customerRequestDTO.getInterestRate() != null;
    }

    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
        if(!isValidRequest(customerRequestDTO))
            throw new BusinessException(ExceptionSeverity.ERROR, "Request is not valid. Please check your request.");

        double creditLimit = calculateCreditLimit(customerRequestDTO.getNetIncome(), customerRequestDTO.getCreditScore());
        isCreditAvailable(customerRequestDTO, creditLimit);

        Customer customer = prepareCustomer(customerRequestDTO, creditLimit);
        Loan loan = getLoan(customerRequestDTO, customer);
        prepareLoanInstallments(loan);

        associateLoanWithCustomer(customer, loan);
        Customer savedCustomer = customerRepository.save(customer);

        return CustomerMapper.INSTANCE.convertToDTO(savedCustomer);
    }

    private void prepareLoanInstallments(Loan loan) {
        BigDecimal installmentAmount = BigDecimal.valueOf(loan.getLoanAmount())
                .multiply(BigDecimal.ONE.add(BigDecimal.valueOf(loan.getInterestRate())))
                .divide(BigDecimal.valueOf(loan.getNumberOfInstallments()), 2, RoundingMode.HALF_UP);

        for (int i = 0; i < loan.getNumberOfInstallments(); i++) {
            LoanInstallment loanInstallment = LoanInstallment.builder()
                    .amount(installmentAmount.doubleValue())
                    .dueDate(LocalDate.now().withDayOfMonth(1).plusMonths(i))
                    .isPaid(false)
                    .loan(loan)
                    .build();
            loan.getInstallments().add(loanInstallment);
        }
    }

    private Customer prepareCustomer(CustomerRequestDTO customerRequestDTO, double creditLimit) {
        return customerRepository.findByIdentityNumber(customerRequestDTO.getIdentityNumber())
                .map(existingCustomer -> updateCustomer(existingCustomer, customerRequestDTO, creditLimit))
                .orElseGet(() -> createNewCustomer(customerRequestDTO, creditLimit));
    }

    private Customer updateCustomer(Customer customer, CustomerRequestDTO request, double creditLimit) {
        double availableLimit = customer.getCreditLimit() - customer.getUsedCreditLimit();
        if (availableLimit < request.getRequestedLoanAmount()) {
            throw new BusinessException(ExceptionSeverity.ERROR, "Requested loan amount exceeds available credit limit.");
        }
        customer.setCreditLimit(creditLimit);
        customer.setUsedCreditLimit(customer.getUsedCreditLimit() + request.getRequestedLoanAmount());
        return customer;
    }

    private Customer createNewCustomer(CustomerRequestDTO request, double creditLimit) {
        return Customer.builder()
                .identityNumber(request.getIdentityNumber())
                .name(request.getName())
                .surname(request.getSurname())
                .creditLimit(creditLimit)
                .usedCreditLimit(request.getRequestedLoanAmount())
                .build();
    }

    private static Loan getLoan(CustomerRequestDTO customerRequestDTO,Customer customer) {
        return Loan.builder()
                .loanAmount(customerRequestDTO.getRequestedLoanAmount())
                .numberOfInstallments(customerRequestDTO.getRequestedLoanTerm())
                .interestRate(customerRequestDTO.getInterestRate())
                .createDate(LocalDate.now())
                .isPaid(false)
                .interestRate(customerRequestDTO.getInterestRate())
                .installments(new ArrayList<>())
                .customer(customer)
                .build();
    }

    private void associateLoanWithCustomer(Customer customer, Loan loan) {
        if (customer.getLoans() == null) {
            customer.setLoans(new ArrayList<>());
        }
        customer.getLoans().add(loan);
    }

    private void isCreditAvailable(CustomerRequestDTO customerRequestDTO, Double creditLimit) {
        if(customerRequestDTO.getRequestedLoanAmount().compareTo(creditLimit) > 0) {
            throw new BusinessException(ExceptionSeverity.ERROR, "Requested loan amount is higher than available credit limit.");
        }
        if(!isLoanTermValid(customerRequestDTO.getRequestedLoanTerm())) {
            throw new BusinessException(ExceptionSeverity.ERROR, "Requested loan term is not valid.");
        }
        if(!isInterestRateValid(customerRequestDTO.getInterestRate())) {
            throw new BusinessException(ExceptionSeverity.ERROR, "Interest rate is not valid.");
        }
    }

}
