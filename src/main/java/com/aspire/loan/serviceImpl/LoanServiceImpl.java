package com.aspire.loan.serviceImpl;

import com.aspire.loan.enums.LoanStatus;
import com.aspire.loan.enums.UserType;
import com.aspire.loan.exceptions.LoanNotApprovedException;
import com.aspire.loan.exceptions.LoanNotFoundException;
import com.aspire.loan.exceptions.UserNotAuthorizedException;
import com.aspire.loan.exceptions.UserNotFoundException;
import com.aspire.loan.model.CustomerLoanInfo;
import com.aspire.loan.model.LoanRepaymentInfo;
import com.aspire.loan.model.User;
import com.aspire.loan.repository.LoanRepository;
import com.aspire.loan.repository.RepaymentRepository;
import com.aspire.loan.repository.UserRepository;
import com.aspire.loan.services.LoanService;
import com.aspire.loan.utility.LoanUtility;
import com.aspire.loan.utility.UserUtility;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RepaymentRepository repaymentRepository;

    @Override
    public List<CustomerLoanInfo> getAllLoans(long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id: "+ userId+" does not exist"));
        UserUtility.isUserAuthentic(user, password);

        if(!user.getType().equals(UserType.ADMIN))
                throw new UserNotAuthorizedException("User with id: "+userId+ " is not authorized to query all loans");
        List<CustomerLoanInfo> loans = loanRepository.findAll();
        return loans;
    }

    @Override
    public List<CustomerLoanInfo> getLoansByUserId(long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id: "+ userId+" does not exist"));
        UserUtility.isUserAuthentic(user, password);
        List<CustomerLoanInfo> loans = user.getLoans();
        return loans;

    }

    @Override
    public CustomerLoanInfo createLoan(CustomerLoanInfo customerLoanInfo, long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id: "+ userId+" does not exist"));
        UserUtility.isUserAuthentic(user, password);

        CustomerLoanInfo loan = customerLoanInfo;
        loan.setStatus(LoanStatus.PENDING);
        loan.setCreated_at(new Date());
        List<Double> repayments = LoanUtility.createRepayments(loan);
        int len =0;
        long currentDate=System.currentTimeMillis();
        List<LoanRepaymentInfo> repaymentsList= new ArrayList<>();

        while(len<repayments.size()){
            Date date = new Date( currentDate + (LoanUtility.WEEKLY_TS));
            LoanRepaymentInfo repaymentInfo=repaymentRepository.save(LoanRepaymentInfo.builder()
                    .amount(repayments.get(len))
                    .deadline_date(date)
                    .status(LoanStatus.PENDING)
                    .build());
            repaymentsList.add(repaymentInfo);
            currentDate=date.getTime();
            len++;
        }

        loan.setRepayments(repaymentsList);

        CustomerLoanInfo savedLoan =loanRepository.save(loan);
        user.getLoans().add(savedLoan);
        userRepository.save(user);
        return savedLoan;
    }

    @Override
    public CustomerLoanInfo getLoanById(long loanId, long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id: "+ userId+" does not exist"));
        UserUtility.isUserAuthorized(user, password, loanId);

        return loanRepository.findById(loanId)
                .orElseThrow(()-> new LoanNotFoundException("Loans with id: "+loanId+" does not exist in system"));
    }

    @Override
    public CustomerLoanInfo repayLoan(long userId, String password, long loanId, double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id: "+ userId+" does not exist"));
        CustomerLoanInfo loan = loanRepository.findById(loanId)
                .orElseThrow(()-> new LoanNotFoundException("Loans with id: "+loanId+" does not exist in system"));

        UserUtility.isUserAuthorized(user, password, loanId);

        // check if loan is Approved
        if(loan.getStatus().equals(LoanStatus.APPROVED)) {

            LoanUtility.repayLoan(loanId, loan);
            boolean isLoanPaidFully = LoanUtility.isLoanFullyPaid(loan);

            if(isLoanPaidFully)
                loan.setStatus(LoanStatus.PAID);

            CustomerLoanInfo savedLoan = loanRepository.save(loan);
            return savedLoan;
        } else{
            throw new LoanNotApprovedException("Loan with id: "+loanId+" is not yet approved by any Admin. Please retry once it is approved");
        }
    }

    @Override
    public CustomerLoanInfo approveLoan(long userId, String password, long loanId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id: "+ userId+" does not exist"));
        CustomerLoanInfo loan = loanRepository.findById(loanId)
                .orElseThrow(()-> new LoanNotFoundException("Loans with id: "+loanId+" does not exist in system"));

        // For Admin we are interested in just checking for authentication
        UserUtility.isUserAuthentic(user, password);

        if(user.getType().equals(UserType.ADMIN)){
            List<LoanRepaymentInfo> repaymentInfos=loan.getRepayments();
            for(int repay=0;repay<repaymentInfos.size();repay++){
                repaymentInfos.get(repay).setStatus(LoanStatus.APPROVED);
            }
            loan.setStatus(LoanStatus.APPROVED);
            CustomerLoanInfo savedLoan= loanRepository.save(loan);
            return savedLoan;
        }else{
            throw new UserNotAuthorizedException("User with id: "+ userId+ " is not authorized to approve the loan.Please get it approved by an Admin.");
        }
    }




}
