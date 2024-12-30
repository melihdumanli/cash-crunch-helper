package com.bank.cash_crunch_helper.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponseDTO {
    private Long id;
    private Long customerId;
    private Double loanAmount;
    private Integer numberOfInstallments;
    private LocalDate createDate;
    private Boolean isPaid;
    private Double interestRate;
    private List<InstallmentResponseDTO> installments;

}
