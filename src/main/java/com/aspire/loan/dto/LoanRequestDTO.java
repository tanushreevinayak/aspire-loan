package com.aspire.loan.dto;

import com.aspire.loan.model.CustomerLoanInfo;
import com.sun.istack.Nullable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoanRequestDTO {

    @Nullable
    private double amount;

    @Nullable
    private CustomerLoanInfo loan;

}
