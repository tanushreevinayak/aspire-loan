package com.aspire.loan.repository;

import com.aspire.loan.model.LoanRepaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepaymentRepository extends JpaRepository<LoanRepaymentInfo, Long> {
    // crud database methods

}
