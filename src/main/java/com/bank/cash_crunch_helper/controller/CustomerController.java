package com.bank.cash_crunch_helper.controller;

import com.bank.cash_crunch_helper.dto.request.CustomerRequestDTO;
import com.bank.cash_crunch_helper.dto.response.CustomerResponseDTO;
import com.bank.cash_crunch_helper.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Operation(summary = "This method is used to get customer info by customer identity number", description = "Enter the customer national Id to get customer info.")
    @GetMapping("/get-customer-info-by-national-id")
    public ResponseEntity<CustomerResponseDTO> getCustomerInfoByNationalId(Long nationalId) {
        return ResponseEntity.ok(customerService.findCustomerByNationalId(nationalId));
    }

    @Operation(summary = "This method is used to add a new customer and a loan to the customer.", description = "Please enter the customer info and loan info.")
    @PostMapping("/add-customer-and-loan")
    public ResponseEntity<CustomerResponseDTO> addCustomerAndLoan(CustomerRequestDTO customerRequestDTO) {
        return ResponseEntity.ok(customerService.createCustomer(customerRequestDTO));
    }

}
