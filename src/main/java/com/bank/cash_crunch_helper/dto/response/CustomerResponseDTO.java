package com.bank.cash_crunch_helper.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private String name;
    private String surname;
    private Double creditLimit;
    private Double usedCreditLimit;
    private List<Long> loanIdList;
}
