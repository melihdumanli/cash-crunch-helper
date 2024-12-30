package com.bank.cash_crunch_helper.mapper;

import com.bank.cash_crunch_helper.dbmodel.Customer;
import com.bank.cash_crunch_helper.dbmodel.Loan;
import com.bank.cash_crunch_helper.dto.response.CustomerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    default CustomerResponseDTO convertToDTO(Customer customer) {
        if(customer == null) {
            return null;
        }
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setSurname(customer.getSurname());
        dto.setCreditLimit(customer.getCreditLimit());
        dto.setUsedCreditLimit(customer.getUsedCreditLimit());
        dto.setLoanIdList(customer.getLoans().stream().map(Loan::getId).collect(Collectors.toList()));
        return dto;
    };

    default List<CustomerResponseDTO> convertToDTOList(List<Customer> customers) {
        if(customers == null) {
            return null;
        }
        return customers.stream().map(this::convertToDTO).collect(Collectors.toList());
    };
}
