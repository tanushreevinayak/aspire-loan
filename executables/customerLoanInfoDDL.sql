CREATE TABLE `customer_loan_info` (
      `loan_id` bigint(20) NOT NULL AUTO_INCREMENT,
      `loan_amount` double NOT NULL,
      `created_at` datetime NOT NULL,
      `loan_status` varchar(255) DEFAULT NULL,
      `loan_term` int(11) NOT NULL,
      PRIMARY KEY (`loan_id`));
