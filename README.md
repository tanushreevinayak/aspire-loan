# Project Name
> The Aspire loan application is meant for authenticated customers have the facility to create and repay loans after they are approved by an Admin.

## Table of Contents
* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Setup](#setup)
* [Usage](#usage)
* [Contact](#contact)

## General Information
- This is an MVP loan application with facility to create the loans by Customers, while the root permissions are restricted to Admin.
- A user is authorized on each request to make sure customer is authentic and authorized to perform actions. Actions like create a user, create a loan(for self only), repay loan(for self only), view loans(for self only, update userInfo/LoanInfo((for self only) are provided to the Customer.
- Customer can create and repay their own loans on a weekly basis(customer will not be able to see anyone else's loans due to authentication and authorization checks).
- The rights to view/update/delete all/any UserInfo, view/update all/any LoanInfo, approve any Loan is restricted to Admin only.
- The aim is to create a loan application with fixed weekly basis repayments for terms and amount mentioned in Customer Loan creation request. This Customer will always be authenticated and authorized to perform any action or an errenous Https response will be sent in case of any violations.


## Technologies Used
- [_Jdk 11_](https://www.oracle.com/in/java/technologies/javase/jdk11-archive-downloads.html)
- [_Spring-boot_](https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html)
- [_MySQL_](https://www.mysql.com/)
- [_Lombok_](https://projectlombok.org/)
- Mockito
- Jacoco
- [_Maven_](https://maven.apache.org/install.html)
- AES encryption


## Features
### Functional
- Only Authenticated Users allowed (AES Encryption and Base64 encoding to store in DB)
- Users need to authorize for viewing/updating other user's data(Only Admins allowed)
- User can create loan with term and amount data only
- User can choose to pay repayments for their respective loans on weekly basis
- User CRUD operations
- Loan Create/View/Repay/Approve operations
- Custom Exceptions for any Violations (e.g. Customer trying to approve a loan, or, Customer requesting to view other user's data)
### Non-Functional
- Modular Design with User and Loan resources
- REST API architecture followed for seamless integration
- JUnit test cases with Mockito
- Swagger documentation (http://localhost:8080/aspire/loan_app/swagger-ui/index.html#/)
- Script to install the app anywhere seamlessly

## Setup
#### Requirements
- JDK 11 (references above)
- MySQL (references above)

#### Dependencies
- Spring-Boot 2.7.0
- Maven 
- Lombok
- Spring-Boot-Starter-Test
- Spring-doc-open-api-ui (for Swagger)

All these dependencies can be found in application directory 'loan' at the path : /loan/pom.xml . 
Running the script will take care of the dependencies and loan application installation (except Java and MySQL, which needs to be installed manually). 
#### Prerequisites
-  Java 11 and MySQL should be installed or it returns error if not found

### Run the Script
- Single step of running this script will launch the loan application on your localhost at port 8080.
- On Terminal/Shell, `cd` into the `executables` folder containing `loanAppInstallScript.sh` file.
- Now run the `loanAppInstallScript.sh` file by using following command:
  `./loanAppInstallScript.sh`
  Alternatively, use `sh loanAppInstallScript.sh`
- Enter MySQL password for the user created as directed below in [Usage](#usage).

### Steps followed in the Script
- Drops any existing MySQL database and MySQL user('aspire_root_loan_user') if already exists, to avoid duplication errors
- Creates a new MySQL user('aspire_root_loan_user') with password('aspire@1') along with the new database(aspire_loan)
  ##### application.properties
    ```spring.datasource.url=jdbc:mysql://localhost:3306/aspire_loan
    spring.datasource.username=root
    spring.datasource.password=aspire@1
    
    spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect
    
    spring.jpa.hibernate.ddl-auto=update
    server.servlet.context-path=/aspire/loan_app/
    springdoc.api-docs.path=/api-docs
    ```
- Grants all privileges on database 'aspire_loan' to our created user 'aspire_root_loan_user'
 
- Runs the following commands to create `MySQL tables`:
#### `1. Users Table`
```CREATE TABLE `users` (
   `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
   `created_at_timestamp` datetime NOT NULL,
   `name` varchar(255) DEFAULT NULL,
   `password` varchar(255) NOT NULL,
   `user_type` varchar(255) DEFAULT NULL,
   PRIMARY KEY (`user_id`)
 )```
 #### `2. Loan Table`
 ```CREATE TABLE `customer_loan_info` (
      `loan_id` bigint(20) NOT NULL AUTO_INCREMENT,
      `loan_amount` double NOT NULL,
      `created_at` datetime NOT NULL,
      `loan_status` varchar(255) DEFAULT NULL,
      `loan_term` int(11) NOT NULL,
      PRIMARY KEY (`loan_id`)
    )```
#### `3. User_Loan Table`
```CREATE TABLE `customer_loans` (
     `user_id` bigint(20) NOT NULL,
     `loan_id` bigint(20) NOT NULL,
     UNIQUE KEY `UK_r88giqecolrq3v4gaad3dvfae` (`loan_id`),
     KEY `FKapypo2qtr2ww0xoqrxbf24g0u` (`user_id`),
     CONSTRAINT `FK7q8r3xej1bgyy7mgas4dq21wc` FOREIGN KEY (`loan_id`) REFERENCES `customer_loan_info` (`loan_id`),
     CONSTRAINT `FKapypo2qtr2ww0xoqrxbf24g0u` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
   )```
#### `4. Repayment_Info Table`
```CREATE TABLE `loan_repayment_info` (
     `loan_repayment_id` bigint(20) NOT NULL AUTO_INCREMENT,
     `total_repayment_amount` double DEFAULT NULL,
     `deadline_date` date DEFAULT NULL,
     `remaining_amount` double DEFAULT NULL,
     `repayment_status` varchar(255) DEFAULT NULL,
     PRIMARY KEY (`loan_repayment_id`)
   )```
#### `5. Loan_Repayments table`
```CREATE TABLE `loan_repayments` (
     `loan_id` bigint(20) NOT NULL,
     `loan_repayment_id` bigint(20) NOT NULL,
     UNIQUE KEY `UK_g38c67mh0f076nwkkr8elbava` (`loan_repayment_id`),
     KEY `FKmlbm4l6bdh7ud97afr1tgchpv` (`loan_id`),
     CONSTRAINT `FKmlbm4l6bdh7ud97afr1tgchpv` FOREIGN KEY (`loan_id`) REFERENCES `customer_loan_info` (`loan_id`),
     CONSTRAINT `FKrh02fmm5xydtemskkxke32lor` FOREIGN KEY (`loan_repayment_id`) REFERENCES `loan_repayment_info` (`loan_repayment_id`)
   )```
   
- Creates an Admin user ("admin") with password ("admin@1") and following details:

```insert into aspire_loan.users(user_type,name, password, created_at_timestamp) VALUES("ADMIN", "admin", TO_BASE64(AES_ENCRYPT('admin@1','secret-key-12345')), CURRENT_TIMESTAMP());```
   
  Please see, keeping things simple for MVP, there is only one Admin with above details for entire system and any new user being added is always a Customer.
- Run the application JAR on your system.

## Usage
- Client should already have the pre requisites installed
- Upon running the script, the client will first be prompted for a password, please use your root MySQL password to get through this point.
- Script will run all the commands successfully and display on client's terminal when the application jar is launched
- Client may lookup the REST APIs exposed on Swagger Documentation (http://localhost:8080/aspire/loan_app/swagger-ui/index.html#/)

### Common Errors And Resolution

`/org.springframework.boot.web.server.PortInUseException: Port 8080 is already in use`

needs following fix:
- `lsof -i:8080`

This command fetches multiple processes running at port 8080. Fetch all process ids from Column `PID` of previous command result.
- Kill these proccesses with command `kill -9 {PID}` e.f. kill -9 8411 (where 8411 is one of the PIDs).
- Now try re-running the script, it should run.


### APIs
- Please make sure all POST/PUT API requests are sent with `Content-Type: application/json` in the Header.
- All user apis except create user(POST /users) would require an Authentication_id(the user id whi is requesting data) and Authentication_password(request user's password in order to authenticate the user against saved password in the system);
- An example: If a user with Id 1 wants to fetch user info for userId 2 (please make sure the user entries are existing in the database), follow following details:
  
  `GET http://localhost:8080/aspire/loan_app/users/2`
  
  Headers details as follows:
  
```[
{
    Authorization_id:1
    Authorization_password:admin@1
}
```
  
  Normally, any user querying about other user would not be allowed, but since the querying user (details provided in Headers) is an Admin, it would allow if user with userId 2 exists, else returns NOT_FOUND error.
  
- Another example of adding a user would be as follows:

  `POST http://localhost:8080/aspire/loan_app/users`
  
  NO Headers required
  
  Body with raw JSON data as follows:
  
    ```[
  {
        "name":"amar",
        "password":"amar@234"
    }
     ```
  Running this will give you a response like following:
  
  ```[
  {
      "id": 2,
      "name": "amar",
      "type": "CUSTOMER",
      "created_at_timestamp": "2023-07-23T12:28:04.509+00:00"
  }
    ```
Please see, any new user being added is always fixed as a customer.

- All Loan apis can also be queried in same fashion. 
- An example would be where user with userId 2 wants to fetch all his/her loans as:
  
  `GET http://localhost:8080/aspire/loan_app/loans `
  
  Headers details as follows:
  
  ```[
  {
      Authorization_id:1
      Authorization_password:admin@1
  }
  ```
  
  it will fetch a response like :
  ```
  [
     {
         "id": 1,
         "repayments": [
             {
                 "id": 1,
                 "amount": 4000.0,
                 "deadline_date": "2023-07-30",
                 "remainingAmount": 0.0,
                 "status": "PENDING"
             },
             {
                 "id": 2,
                 "amount": 4000.0,
                 "deadline_date": "2023-08-06",
                 "remainingAmount": 0.0,
                 "status": "PENDING"
             },
             {
                 "id": 3,
                 "amount": 4000.0,
                 "deadline_date": "2023-08-13",
                 "remainingAmount": 0.0,
                 "status": "PENDING"
             }
         ],
         "term": 3,
         "created_at": "2023-07-23T12:35:21.000+00:00",
         "amount": 12000.0,
         "status": "PENDING"
     }
  ] 
  ```
  
- Another example would be to create a new loan with authentication user details as follows:
 
 `POST http://localhost:8080/aspire/loan_app/loans/`
 
 with Headers as follows 
  
  ```[
  {
      Authorization_id:1
      Authorization_password:admin@1
  }
  ```
  
  and the Body json object as:
  
  ```[
  {
      "term":3,
      "amount":12000
  }
```
  
  This will send a response like :
  ```
[
    {
         "id": 1,
         "repayments": [
             {
                 "id": 1,
                 "amount": 4000.0,
                 "deadline_date": "2023-07-30T12:35:20.770+00:00",
                 "remainingAmount": 0.0,
                 "status": "PENDING"
             },
             {
                 "id": 2,
                 "amount": 4000.0,
                 "deadline_date": "2023-08-06T12:35:20.770+00:00",
                 "remainingAmount": 0.0,
                 "status": "PENDING"
             },
             {
                 "id": 3,
                 "amount": 4000.0,
                 "deadline_date": "2023-08-13T12:35:20.770+00:00",
                 "remainingAmount": 0.0,
                 "status": "PENDING"
             }
         ],
         "term": 3,
         "created_at": "2023-07-23T12:35:20.769+00:00",
         "amount": 12000.0,
         "status": "PENDING"
    }
  ]
  ```
  
- Please see that the USER_TYPE or LOAN_STATUS are not updatable fields in this MVP. For any request the user type is always CUSTOMER while loan status is always PENDING in start before being approved by the admin and finally, being paid by the customer.
- For more APIs, please follow the Swagger link provided above.
- Please Note, a single repayment request(`http://localhost:8080/aspire/loan_app/loans/{loanId}/{amount}/repayment`) (irrespective of amount field in the request)
will always only pay the single pending weekly installment. So, to pay entire loan, we must send requests for total number of terms(eg. 3 in above example with LoanId 1);
## CustomExceptions
```
* UserNotAuthenticatedException(HttpStatus.UNAUTHORIZED)
* UserNotAuthorizedException(HttpStatus.FORBIDDEN)
* UserNotFoundException(HttpStatus.NOT_FOUND)
* InvalidInputException(HttpStatus.BAD_REQUEST)
* LoanNotFoundException(HttpStatus.NOT_FOUND)
* LoanNotApprovedException(HttpStatus.PRECONDITION_FAILED)
```
 
## Contact
Created by Tanushree Vinayak 
