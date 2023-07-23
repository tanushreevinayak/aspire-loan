package com.aspire.loan.utility;

import com.aspire.loan.enums.LoanStatus;
import com.aspire.loan.exceptions.InvalidInputException;
import com.aspire.loan.model.CustomerLoanInfo;
import com.aspire.loan.model.LoanRepaymentInfo;
import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@UtilityClass
public class LoanUtility {

    public static final int WEEKLY_TS=86400 * 7 * 1000;

    public List<Double> createRepayments(CustomerLoanInfo loan){
        double amount = loan.getAmount();
        int term = loan.getTerm();

        if(amount <=0 || term <= 0)
            throw new InvalidInputException("Please check the loan amount= "+amount+" or loan term= "+term+" not to be Zero");

        int total_so_far=0;
        double remainder =0;
        double amount_dup=amount;
        List<Double> repayments=new ArrayList<>();
        final DecimalFormat decfor = new DecimalFormat("0.00");

        while(amount_dup>0 && term>0){
            double divisor = Double.valueOf(decfor.format(amount_dup/(term--)));
            repayments.add(divisor);
            amount_dup-=divisor;
        }

        return repayments;
    }

    public boolean isAuthenticUser(String inputPassword, String actualPassword){
        return inputPassword.equals(actualPassword);
    }

    public void repayLoan(Long loanId, CustomerLoanInfo loan){
        List<LoanRepaymentInfo> repayments = loan.getRepayments();
        Collections.sort(repayments, new SortbyDeadlineDate());

        for(int repay=0;repay<repayments.size();repay++){
            LoanRepaymentInfo repayInfo = repayments.get(repay);
            if(repayInfo.getStatus().equals(LoanStatus.APPROVED)){
                repayInfo.setStatus(LoanStatus.PAID);
                break;
            }
        }
    }

    public boolean isLoanFullyPaid(CustomerLoanInfo loan){
        boolean isFullyPaidLoan = true;
        // check if all repayments done
        for(int repayment=0;repayment<loan.getRepayments().size();repayment++){
            if(!loan.getRepayments().get(repayment).getStatus().equals(LoanStatus.PAID)){
                isFullyPaidLoan=false;
                break;
            }
        }
        return isFullyPaidLoan;
    }
}

class SortbyDeadlineDate implements Comparator<LoanRepaymentInfo> {
    public int compare(LoanRepaymentInfo a, LoanRepaymentInfo b)
    {
        return a.getDeadline_date().compareTo(b.getDeadline_date());
    }
}
