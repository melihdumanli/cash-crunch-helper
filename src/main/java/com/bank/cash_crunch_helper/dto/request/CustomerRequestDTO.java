package com.bank.cash_crunch_helper.dto.request;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDTO {
    private Long identityNumber;
    private String name;
    private String surname;
    private Double netIncome;
    private Integer creditScore;
    private Double requestedLoanAmount;
    private Integer requestedLoanTerm;
    private Double interestRate;
}
