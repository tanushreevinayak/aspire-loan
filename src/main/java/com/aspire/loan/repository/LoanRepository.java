package com.aspire.loan.repository;

import com.aspire.loan.model.CustomerLoanInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<CustomerLoanInfo, Long> {
    // crud database methods

}
