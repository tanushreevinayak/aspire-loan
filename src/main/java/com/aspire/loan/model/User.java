package com.aspire.loan.model;


import com.aspire.loan.enums.UserType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    @Column(name ="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="name")
    private String name;

    @Column(name="password", nullable=false)
    @Convert(converter = AttributeEncryptor.class)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name="user_type")
    private UserType type;

    @Column(name="created_at_timestamp" , nullable = false, updatable = false)
    @CreationTimestamp
    private java.util.Date created_at_ts;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "customer_loans",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "loan_id"))
    private List<CustomerLoanInfo> loans;

//    @Column(name="created_at_timestamp")
//    @Temporal(TemporalType.TIMESTAMP)
//    private java.util.Date last_login_ts;

    // account no? or pan id?

}
