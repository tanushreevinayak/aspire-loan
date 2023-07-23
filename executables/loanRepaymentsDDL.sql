CREATE TABLE `loan_repayments` (
  `loan_id` bigint(20) NOT NULL,
  `loan_repayment_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_g38c67mh0f076nwkkr8elbava` (`loan_repayment_id`),
  KEY `FKmlbm4l6bdh7ud97afr1tgchpv` (`loan_id`),
  CONSTRAINT `FKmlbm4l6bdh7ud97afr1tgchpv` FOREIGN KEY (`loan_id`) REFERENCES `customer_loan_info` (`loan_id`),
  CONSTRAINT `FKrh02fmm5xydtemskkxke32lor` FOREIGN KEY (`loan_repayment_id`) REFERENCES `loan_repayment_info` (`loan_repayment_id`)
);
