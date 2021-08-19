show databases;
create database bank_system;
use bank_system;
show tables;

create table customers (id int ,name varchar(20),email varchar(25),phoneNumber numeric(10),address varchar(100),kycVerificationStatus BOOLEAN,adharNumber varchar(12),panNumber varchar(10));

alter table customers add account longtext;
	
create table accounts (accountNumber long ,Holdername varchar(20),accountType varchar(20),bankName varchar(12),branchCode varchar(10),ifscCode varchar(10),balance double );

create table transaction(id int, amount double, sentByAccNo varchar(100), sentByName varchar(100), receiverAccNo varchar(100), receiverName varchar(100), transactionType varchar(55), date varchar(55));

alter table accounts add customerId int;

alter table accounts change customerId id int;

ALTER TABLE accounts CHANGE id id int NOT NULL;

ALTER TABLE customers ADD CONSTRAINT PrimaryKey PRIMARY KEY (id);

ALTER TABLE customers MODIFY COLUMN id INT auto_increment;

ALTER TABLE customers AUTO_INCREMENT=9999;


ALTER TABLE accounts ADD CONSTRAINT accounts_unique UNIQUE (id);

ALTER TABLE accounts ADD FOREIGN KEY (id) REFERENCES customers(id);

alter table transaction change id TransactionID varchar(30);



