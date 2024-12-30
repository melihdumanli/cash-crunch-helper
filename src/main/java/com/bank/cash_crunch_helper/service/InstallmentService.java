package com.bank.cash_crunch_helper.service;

import com.bank.cash_crunch_helper.dataaccess.LoanInstallmentRepository;
import com.bank.cash_crunch_helper.dbmodel.LoanInstallment;
import com.bank.cash_crunch_helper.dto.response.InstallmentResponseDTO;
import com.bank.cash_crunch_helper.mapper.InstallmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstallmentService {

    private final LoanInstallmentRepository installmentRepository;
    public List<InstallmentResponseDTO> findInstallmentsByLoanId(Long loanId) {
        List<LoanInstallment> installments = installmentRepository.findInstallmentsByLoanId(loanId);

        return InstallmentMapper.INSTANCE.convertToDTOList(installments);
    }
}
