package com.aspire.loan.controller;

import com.aspire.loan.model.CustomerLoanInfo;
import com.aspire.loan.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @GetMapping("/all")
    public ResponseEntity<List<CustomerLoanInfo>> getAllLoans(@RequestHeader(name="Authorization_id") long userId,
                                                              @RequestHeader(name="Authorization_password") String password){
        List<CustomerLoanInfo> customerLoans= loanService.getAllLoans(userId, password);
        return new ResponseEntity<>(customerLoans, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerLoanInfo> createLoan(@RequestBody CustomerLoanInfo customerLoanInfo,
                                                       @RequestHeader(name="Authorization_id") long userId,
                                                       @RequestHeader(name="Authorization_password") String password){
        CustomerLoanInfo custLoan = loanService.createLoan(customerLoanInfo, userId, password);
        return new ResponseEntity<>(custLoan, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CustomerLoanInfo>> getLoansByUserId(@RequestHeader(name="Authorization_id") long userId,
                                                                   @RequestHeader(name="Authorization_password") String password){
        List<CustomerLoanInfo> loans = loanService.getLoansByUserId(userId, password);
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<CustomerLoanInfo> getLoanById(@PathVariable("loanId") long loanId,
                                                        @RequestHeader(name="Authorization_id") long userId,
                                                        @RequestHeader(name="Authorization_password") String password){
        CustomerLoanInfo customerLoanInfo=loanService.getLoanById(loanId, userId, password);
        return new ResponseEntity<>(customerLoanInfo, HttpStatus.OK);
    }

    @PostMapping("/{loanId}/{amount}/repayment")
    public ResponseEntity<CustomerLoanInfo> repayLoan(@PathVariable("loanId") long loanId,
                                                      @RequestHeader(name="Authorization_id") long userId,
                                                      @RequestHeader(name="Authorization_password") String password,
                                                      @PathVariable("amount") double amount){
        CustomerLoanInfo loan =loanService.repayLoan(userId, password, loanId, amount);
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }

    @PutMapping("/{loanId}/approval")
    public ResponseEntity<CustomerLoanInfo> approveLoan(@RequestHeader(name="Authorization_id") long userId,
                                                        @RequestHeader(name="Authorization_password") String password,
                                                        @PathVariable("loanId") long loanId){
        CustomerLoanInfo loan =loanService.approveLoan(userId, password, loanId);
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }
    
}
