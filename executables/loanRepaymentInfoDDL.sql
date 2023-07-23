CREATE TABLE `loan_repayment_info` (
     `loan_repayment_id` bigint(20) NOT NULL AUTO_INCREMENT,
     `total_repayment_amount` double DEFAULT NULL,
     `deadline_date` date DEFAULT NULL,
     `remaining_amount` double DEFAULT NULL,
     `repayment_status` varchar(255) DEFAULT NULL,
     PRIMARY KEY (`loan_repayment_id`)
   );
