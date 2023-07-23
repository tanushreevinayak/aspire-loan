package com.aspire.loan.services;

import com.aspire.loan.model.CustomerLoanInfo;

import java.util.List;

public interface LoanService {

    List<CustomerLoanInfo> getAllLoans(long userId, String password);

    List<CustomerLoanInfo> getLoansByUserId(long userId, String password);

    CustomerLoanInfo createLoan(CustomerLoanInfo customerLoanInfo, long userId, String password);

    CustomerLoanInfo getLoanById(long loanId, long userId, String password);

    CustomerLoanInfo repayLoan(long userId, String password,long loanId, double amount);

    CustomerLoanInfo approveLoan(long userId, String password, long loanId);
}
