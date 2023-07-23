package com.aspire.loan.model;

import com.aspire.loan.enums.LoanStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "loan_repayment_info")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanRepaymentInfo {

    @Id
    @Column(name ="loan_repayment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="total_repayment_amount")
    @Setter(AccessLevel.NONE)
    private double amount;

    @Column(name="deadline_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date deadline_date;

    @Column(name="remaining_amount")
    @Setter(AccessLevel.NONE)
    private double remainingAmount;

    @Column(name="repayment_status")
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
}
