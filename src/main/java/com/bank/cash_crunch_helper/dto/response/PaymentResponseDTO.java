package com.bank.cash_crunch_helper.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private int installmentsPaid;
    private double totalSpent;
    private boolean isLoanFullyPaid;
    private double refundAmount;

}
