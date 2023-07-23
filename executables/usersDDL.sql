USE aspire_loan;

CREATE TABLE users (
   user_id bigint(20) NOT NULL AUTO_INCREMENT,
   created_at_timestamp datetime NOT NULL,
   name varchar(255) DEFAULT NULL,
   password varchar(255) NOT NULL,
   user_type varchar(255) DEFAULT NULL,
   PRIMARY KEY (user_id));
