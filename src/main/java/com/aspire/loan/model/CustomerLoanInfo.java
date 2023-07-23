package com.aspire.loan.model;

import com.aspire.loan.enums.LoanStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "customer_loan_info")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerLoanInfo {

    @Column(name="loan_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "loan_repayments",
            joinColumns = @JoinColumn(name = "loan_id"),
            inverseJoinColumns = @JoinColumn(name = "loan_repayment_id"))
    private List<LoanRepaymentInfo> repayments;

    @Column(name="loan_term", nullable = false)
    @Setter(AccessLevel.NONE)
    private int term;

    @Column(name="created_at", nullable = false, updatable = false)
    @CreatedDate
    private java.util.Date created_at;

    @Column(name="loan_amount", nullable = false)
    @Setter(AccessLevel.NONE)
    private double amount;

    @Column(name="loan_status")
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

}
