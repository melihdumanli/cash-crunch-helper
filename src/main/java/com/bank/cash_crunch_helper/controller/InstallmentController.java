package com.bank.cash_crunch_helper.controller;

import com.bank.cash_crunch_helper.dto.response.InstallmentResponseDTO;
import com.bank.cash_crunch_helper.service.InstallmentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/installment")
public class InstallmentController {

    private final InstallmentService installmentService;

    @Operation(summary = "This method is used to get all installments by loan id.", description = "Enter the loan Id to get all installments.")
    @GetMapping("/get-by-loan-id/{loanId}")
    public ResponseEntity<List<InstallmentResponseDTO>> getInstallmentsByLoanId(Long loanID) {
        return ResponseEntity.ok(installmentService.findInstallmentsByLoanId(loanID));
    }

}
