package com.bank.cash_crunch_helper.mapper;

import com.bank.cash_crunch_helper.dbmodel.Loan;
import com.bank.cash_crunch_helper.dto.response.InstallmentResponseDTO;
import com.bank.cash_crunch_helper.dto.response.LoanResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LoanMapper {
    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    default LoanResponseDTO convertToDTO(Loan loan) {
        if (loan == null) {
            return null;
        }
        LoanResponseDTO dto = new LoanResponseDTO();
        dto.setId(loan.getId());
        dto.setCustomerId(loan.getCustomer().getId());
        dto.setLoanAmount(loan.getLoanAmount());
        dto.setNumberOfInstallments(loan.getNumberOfInstallments());
        dto.setCreateDate(loan.getCreateDate());
        dto.setIsPaid(loan.getIsPaid());
        dto.setInterestRate(loan.getInterestRate());

        List<InstallmentResponseDTO> installments = loan.getInstallments().stream().map(installment -> {
            InstallmentResponseDTO installmentResponseDTO = new InstallmentResponseDTO();
            installmentResponseDTO.setId(installment.getId());
            installmentResponseDTO.setLoanId(installment.getLoan().getId());
            installmentResponseDTO.setAmount(installment.getAmount());
            installmentResponseDTO.setPaidAmount(installment.getPaidAmount());
            installmentResponseDTO.setDueDate(installment.getDueDate());
            installmentResponseDTO.setPaymentDate(installment.getPaymentDate());
            installmentResponseDTO.setIsPaid(installment.getIsPaid());
            return installmentResponseDTO;
        }).toList();
        dto.setInstallments(installments);
        return dto;
    };
    default List<LoanResponseDTO> convertToDTOList(List<Loan> loans) {
        if (loans == null) {
            return null;
        }
        return loans.stream().map(this::convertToDTO).collect(Collectors.toList());
    };
}
