package com.bank.cash_crunch_helper.mapper;

import com.bank.cash_crunch_helper.dbmodel.LoanInstallment;
import com.bank.cash_crunch_helper.dto.response.InstallmentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface InstallmentMapper {
    InstallmentMapper INSTANCE = Mappers.getMapper(InstallmentMapper.class);

    default InstallmentResponseDTO convertToDTO(LoanInstallment installment) {
        if ( installment == null ) {
            return null;
        }

        InstallmentResponseDTO.InstallmentResponseDTOBuilder installmentResponseDTO = InstallmentResponseDTO.builder();

        installmentResponseDTO.id( installment.getId() );
        installmentResponseDTO.loanId( installment.getLoan().getId() );
        installmentResponseDTO.amount( installment.getAmount() );
        installmentResponseDTO.paidAmount( installment.getPaidAmount() );
        installmentResponseDTO.dueDate( installment.getDueDate() );
        installmentResponseDTO.paymentDate( installment.getPaymentDate() );
        installmentResponseDTO.isPaid( installment.getIsPaid() );

        return installmentResponseDTO.build();
    };

    default List<InstallmentResponseDTO> convertToDTOList(List<LoanInstallment> installments) {
        if ( installments == null ) {
            return null;
        }
        return installments.stream().map(this::convertToDTO).toList();
    };
}
