#!/bin/bash
    username='aspire_root_loan_user'
    password='aspire@1'
    database='aspire_loan'
    
	
    if [ -f /root/.my.cnf ]; then

	mysql -e "DROP DATABASE IF EXISTS ${database};"
	mysql -e "DROP USER ${username}@localhost;"
	mysql  -e "FLUSH PRIVILEGES;"
	
	echo "Creating new user..."	
	mysql -e "CREATE USER ${username}@localhost IDENTIFIED BY '${password}';"
	echo "User successfully created!"

	echo "Creating new MySQL database..."
	mysql -e "CREATE DATABASE ${database} /*\!40100 DEFAULT CHARACTER SET utf8 */;"
	echo "Database successfully created!"

	
	mysql -e "GRANT ALL PRIVILEGES ON ${database}.* TO '${username}'@'localhost';"
	mysql -e "FLUSH PRIVILEGES;"

	echo "Creating users table..."
        mysql -u ${username}  -p${password} $database < usersDDL.sql;	
        echo "users table created successfully!!"

        echo "Creating customer_loan_info table..."
        mysql  -u $username  -p$password $database < customerLoanInfoDDL.sql;
        echo "customer_loan_info table created successfully!!"

        echo "Creating customer_loans table..."
        mysql  -u $username  -p$password $database < customerLoansDDL.sql;
        echo "customer_loans table created successfully!!"

        echo "Creating loan_repayment_info table..."
        mysql  -u $username  -p$password $database < loanRepaymentInfoDDL.sql;
        echo "loan_repayment_info table created successfully!!"

        echo "Creating loan_repayments table..."
        mysql  -u $username  -p$password $database < loanRepaymentsDDL.sql;
        echo "loan_repayments table created successfully!!"
	
	echo "Creating Admin entry for users table..."
        mysql  -u $username  -p$password $database < adminUserCreate.sql;
        echo "Admin created successfully!!"

        echo "All tables created successfully!!"

	echo "launching the loan application"
	java -jar loan.jar
	echo "launched loan application successfully"
	
   else
	echo "Please enter root user MySQL password!"
	echo "Note: password will be hidden when typing"
	read -s rootpasswd

	mysql -uroot -p${rootpasswd}  -e "DROP DATABASE IF EXISTS ${database};"
        mysql -uroot -p${rootpasswd} -e "DROP USER  ${username}@localhost;"
        mysql -uroot -p${rootpasswd}  -e "FLUSH PRIVILEGES;"

	echo "Creating new user..."	
	mysql -uroot -p${rootpasswd} -e "CREATE USER ${username}@localhost IDENTIFIED BY '${password}';"
	echo "User successfully created!"

	echo "Creating new MySQL database..."	
	mysql -uroot -p${rootpasswd} -e "CREATE DATABASE ${database} /*\!40100 DEFAULT CHARACTER SET utf8 */ ;"
	echo "Database successfully created!"
    
	
	echo "Granting ALL privileges on ${database} to ${username}!"
	mysql -uroot -p${rootpasswd} -e "GRANT ALL PRIVILEGES ON ${database}.* TO '${username}'@'localhost';"
	mysql -uroot -p${rootpasswd} -e "FLUSH PRIVILEGES;"
   
	echo "Creating users table..."
        mysql -u ${username}  -p${password} $database < usersDDL.sql;   

        echo "users table created successfully!!"

        echo "Creating customer_loan_info table..."
        mysql  -u $username  -p$password $database < customerLoanInfoDDL.sql;
        echo "customer_loan_info table created successfully!!"

        echo "Creating customer_loans table..."
        mysql  -u $username  -p$password $database < customerLoansDDL.sql;
        echo "customer_loans table created successfully!!"

        echo "Creating loan_repayment_info table..."
        mysql  -u $username  -p$password $database < loanRepaymentInfoDDL.sql;
        echo "loan_repayment_info table created successfully!!"

        echo "Creating loan_repayments table..."
        mysql  -u $username  -p$password $database < loanRepaymentsDDL.sql;
        echo "loan_repayments table created successfully!!"

	echo "Creating Admin entry for users table..."
        mysql  -u $username  -p$password $database < adminUserCreate.sql;
        echo "Admin created successfully!!"

        echo "All tables created successfully!!"

	echo "launching the loan application"
        java -jar loan.jar
        echo "launched loan application successfully"
	
   fi
	
    
    	



	
