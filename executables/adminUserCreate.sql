insert into aspire_loan.users(user_type,name, password, created_at_timestamp) VALUES("ADMIN", "admin", TO_BASE64(AES_ENCRYPT('admin@1','secret-key-12345')), CURRENT_TIMESTAMP());

