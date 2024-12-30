package com.bank.cash_crunch_helper.service;

import com.bank.cash_crunch_helper.dataaccess.LoanRepository;
import com.bank.cash_crunch_helper.dbmodel.Loan;
import com.bank.cash_crunch_helper.dbmodel.LoanInstallment;
import com.bank.cash_crunch_helper.dto.response.LoanResponseDTO;
import com.bank.cash_crunch_helper.dto.response.PaymentResponseDTO;
import com.bank.cash_crunch_helper.exception.BusinessException;
import com.bank.cash_crunch_helper.exception.ExceptionSeverity;
import com.bank.cash_crunch_helper.mapper.LoanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;

    public List<LoanResponseDTO> findLoansByCustomer(Long nationalId) {
        List<Loan> loans = loanRepository.findUnpaidLoansByCustomerId(nationalId);
        if (loans.isEmpty()) {
            throw new BusinessException(ExceptionSeverity.ERROR, "No loan found for the customer");
        }
        return LoanMapper.INSTANCE.convertToDTOList(loans);
    }

    public PaymentResponseDTO payLoan(Long loanId, Double amount) {
        if (amount <= 0) {
            throw new BusinessException(ExceptionSeverity.ERROR, "Amount must be greater than 0");
        }

        Loan loan = loanRepository.findById(loanId).orElse(null);
        if (loan == null) {
            throw new BusinessException(ExceptionSeverity.ERROR, "Loan not found");
        }

        List<LoanInstallment> installments = loan.getInstallments().stream()
                .filter(i -> !i.getIsPaid() && isWithinPayableRange(i.getDueDate()))
                .sorted(Comparator.comparing(LoanInstallment::getDueDate))
                .toList();

        if (installments.isEmpty()) {
            throw new BusinessException(ExceptionSeverity.INFO, "No unpaid installment found for the loan within payable range");
        }

        double remainingAmount = amount;
        int installmentsPaid = 0;
        double totalSpent = 0.0;

        for (LoanInstallment installment : installments) {
            if (remainingAmount >= installment.getAmount()) {
                remainingAmount -= installment.getAmount();
                totalSpent += installment.getAmount();
                installment.setIsPaid(true);
                installmentsPaid++;
            } else {
                break;
            }
        }

        boolean isLoanFullyPaid = loan.getInstallments().stream().allMatch(LoanInstallment::getIsPaid);
        if (isLoanFullyPaid) {
            loan.setIsPaid(true);
        }

        loanRepository.save(loan);

        return new PaymentResponseDTO(installmentsPaid, totalSpent, isLoanFullyPaid,remainingAmount);
    }

    private boolean isWithinPayableRange(LocalDate dueDate) {
        LocalDate today = LocalDate.now();
        LocalDate threeMonthsLater = today.plusMonths(3);
        return !dueDate.isAfter(threeMonthsLater);
    }

}
