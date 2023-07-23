CREATE TABLE `customer_loans` (
     `user_id` bigint(20) NOT NULL,
     `loan_id` bigint(20) NOT NULL,
     UNIQUE KEY `UK_r88giqecolrq3v4gaad3dvfae` (`loan_id`),
     KEY `FKapypo2qtr2ww0xoqrxbf24g0u` (`user_id`),
     CONSTRAINT `FK7q8r3xej1bgyy7mgas4dq21wc` FOREIGN KEY (`loan_id`) REFERENCES `customer_loan_info` (`loan_id`),
     CONSTRAINT `FKapypo2qtr2ww0xoqrxbf24g0u` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
   );
