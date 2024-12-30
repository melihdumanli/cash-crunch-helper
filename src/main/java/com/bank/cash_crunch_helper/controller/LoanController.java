package com.bank.cash_crunch_helper.controller;

import com.bank.cash_crunch_helper.dto.response.LoanResponseDTO;
import com.bank.cash_crunch_helper.dto.response.PaymentResponseDTO;
import com.bank.cash_crunch_helper.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loan")
public class LoanController {

    private final LoanService loanService;

    @Operation(summary = "This method is used to get loans by customer national id", description = "Enter the customer national id to get loans.")
    @GetMapping("/get-loans-by-customer/{nationalId}")
    public ResponseEntity<List<LoanResponseDTO>> getLoansByCustomer(Long nationalId) {
        return ResponseEntity.ok(loanService.findLoansByCustomer(nationalId));
    }

    @Operation(summary = "This method is used to pay loan", description = "Enter the loan id and amount to pay loan.")
    @PostMapping("/pay-loan/{loanId}/{amount}")
    public ResponseEntity<PaymentResponseDTO> payLoan(Long loanId, Double amount) {
        return ResponseEntity.ok(loanService.payLoan(loanId, amount));
    }


}
