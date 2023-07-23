package com.aspire.loan.services;

import com.aspire.loan.dto.LoanRequestDTO;
import com.aspire.loan.enums.LoanStatus;
import com.aspire.loan.enums.UserType;
import com.aspire.loan.exceptions.LoanNotApprovedException;
import com.aspire.loan.exceptions.LoanNotFoundException;
import com.aspire.loan.exceptions.UserNotAuthenticatedException;
import com.aspire.loan.exceptions.UserNotAuthorizedException;
import com.aspire.loan.model.CustomerLoanInfo;
import com.aspire.loan.model.LoanRepaymentInfo;
import com.aspire.loan.model.User;
import com.aspire.loan.repository.LoanRepository;
import com.aspire.loan.repository.RepaymentRepository;
import com.aspire.loan.repository.UserRepository;
import com.aspire.loan.serviceImpl.LoanServiceImpl;
import com.aspire.loan.utility.LoanUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private RepaymentRepository repaymentRepository;

    private LoanServiceImpl loanServiceImpl;
    private CustomerLoanInfo customerLoanInfo;
    private User user1;
    private User adminUser;
    private List<LoanRepaymentInfo> repaymentsInfolist;
    private LoanRequestDTO loanRequestDTOAdmin;
    private List<CustomerLoanInfo> loansList;

    @BeforeEach
    void setUp()
    {
           repaymentsInfolist =new ArrayList<>();
           loansList= new ArrayList<>();
            user1= User.builder()
                .created_at_ts(new Date(System.currentTimeMillis()))
                .id(11L)
                .name("testUser")
                .type(UserType.CUSTOMER)
                .password("userTest@1")
                .build();
           adminUser = User.builder()
                    .created_at_ts(new Date(System.currentTimeMillis()))
                    .id(12L)
                    .name("testUser2")
                    .type(UserType.ADMIN)
                    .password("user2@test")
                   .loans(loansList)
                    .build();

          customerLoanInfo = CustomerLoanInfo.builder()
                  .amount(10000)
                  .created_at(new Date())
                  .id(1L)
                  .status(LoanStatus.PENDING)
                  .term(3)
                  .repayments(new ArrayList<>())
                  .build();

          Date date = new Date(System.currentTimeMillis()+LoanUtility.WEEKLY_TS);

        LoanRepaymentInfo lr1= (LoanRepaymentInfo.builder()
            .deadline_date(date)
            .amount(3333.33)
            .status(LoanStatus.PENDING)
            .id(1L)
            .build());

            date = new Date(date.getTime()+LoanUtility.WEEKLY_TS);
            LoanRepaymentInfo lr2=(LoanRepaymentInfo.builder()
            .deadline_date(date)
            .amount(3333.33)
            .status(LoanStatus.PENDING)
            .id(2L)
            .build());

            date =new Date(date.getTime()+LoanUtility.WEEKLY_TS);
        LoanRepaymentInfo lr3=(LoanRepaymentInfo.builder()
            .deadline_date(date)
            .amount(3333.34)
            .status(LoanStatus.PENDING)
            .id(3L)
            .build());

            repaymentsInfolist.addAll(Arrays.asList(lr1, lr2, lr3));

            customerLoanInfo.getRepayments().addAll(repaymentsInfolist);
            loansList.add(customerLoanInfo);

            this.loanServiceImpl
                    = new LoanServiceImpl(loanRepository, userRepository, repaymentRepository);

    }

    @Test
    void getAllLoans_NotAuthenticatedException()
    {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(adminUser));
        assertThrows(UserNotAuthenticatedException.class, () -> {
            loanServiceImpl.getAllLoans(adminUser.getId(), "fakePassword");
        });
    }

    @Test
    void getAllLoans_UnauthorizedException()
    {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(user1));
        assertThrows(UserNotAuthorizedException.class, () -> {
            loanServiceImpl.getAllLoans(user1.getId(), user1.getPassword());
        });
    }

    @Test
    void getAllLoans_Success()
    {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(adminUser));
        when(loanRepository.findAll()).thenReturn(loansList);
        List<CustomerLoanInfo> loanList = loanServiceImpl.getAllLoans(12L, adminUser.getPassword());
        assertEquals(loanList.size(), 1);
    }

    @Test
    void getLoansByUserId_Unauthenticated()
    {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(user1));
        assertThrows(UserNotAuthenticatedException.class, () -> {
            loanServiceImpl.getLoansByUserId(user1.getId(), "fake");
        });
    }

    @Test
    void getLoansByUserId_Success()
    {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(adminUser));
        List<CustomerLoanInfo> loans = loanServiceImpl.getLoansByUserId(adminUser.getId(), adminUser.getPassword());
        assertEquals(loans.size(), 1);
        assertEquals(loans.get(0).getId(), 1L);
    }

    @Test
    void createLoan_success()
    {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(adminUser));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(adminUser);
        when(loanRepository.save(Mockito.any(CustomerLoanInfo.class))).thenReturn(customerLoanInfo);
        CustomerLoanInfo loan = loanServiceImpl.createLoan(customerLoanInfo, adminUser.getId(), adminUser.getPassword());
        assertEquals(loan.getRepayments().size(), 3);
        List<LoanRepaymentInfo> repaymentsList = loan.getRepayments();
    }

    @Test
    void getLoanById_LoanNotFoundException(){
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(adminUser));
        when(loanRepository.findById(anyLong())).thenThrow(LoanNotFoundException.class);
        assertThrows(LoanNotFoundException.class, () -> {
            loanServiceImpl.getLoanById(15L, adminUser.getId(), adminUser.getPassword());
        });
    }

    @Test
    void getLoanById_success(){
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(adminUser));
        when(loanRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(customerLoanInfo));
        CustomerLoanInfo loan = loanServiceImpl.getLoanById(1L, 12L, adminUser.getPassword());
        assertEquals(loan.getId(), 1L);
    }

    @Test
    void repayLoan_Unauthorized(){
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(user1));
        when(loanRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(customerLoanInfo));
        assertThrows(UserNotAuthorizedException.class, () -> {
            loanServiceImpl.repayLoan(user1.getId(), user1.getPassword(), customerLoanInfo.getId(), customerLoanInfo.getAmount());
        });
    }

    @Test
    void repayLoan_PreconditionFailure(){
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(adminUser));
        when(loanRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(customerLoanInfo));
        assertThrows(LoanNotApprovedException.class, () -> {
            loanServiceImpl.repayLoan(adminUser.getId(), adminUser.getPassword(), customerLoanInfo.getId(), customerLoanInfo.getAmount());
        });

    }

    @Test
    void repayLoan_Success(){
        customerLoanInfo.setStatus(LoanStatus.APPROVED);
        for(int re=0;re<customerLoanInfo.getRepayments().size();re++){
            customerLoanInfo.getRepayments().get(re).setStatus(LoanStatus.APPROVED);
        }
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(adminUser));
        when(loanRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(customerLoanInfo));
        when(loanRepository.save(Mockito.any(CustomerLoanInfo.class))).thenReturn(customerLoanInfo);
        CustomerLoanInfo loan = loanServiceImpl.repayLoan(adminUser.getId(), adminUser.getPassword(), customerLoanInfo.getId(), 3333.33);
        boolean isPaid = false;
        for(int re=0;re<loan.getRepayments().size();re++){
            if(loan.getRepayments().get(re).getStatus().equals(LoanStatus.PAID)) {
                isPaid = true;
                break;
            }
        }

        assertTrue(isPaid);
    }

    @Test
    void approveLoan_Sucess(){
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(adminUser));
        when(loanRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(customerLoanInfo));
        when(loanRepository.save(Mockito.any(CustomerLoanInfo.class))).thenReturn(customerLoanInfo);
        CustomerLoanInfo loan = loanServiceImpl.approveLoan(adminUser.getId(), adminUser.getPassword(), customerLoanInfo.getId());

        assertEquals(loan.getStatus(), LoanStatus.APPROVED);
    }

}
