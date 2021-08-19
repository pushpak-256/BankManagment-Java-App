show databases;
create database bank_system;
use bank_system;
show tables;
create table customers (id int ,name varchar(20),email varchar(25),phoneNumber numeric(10),address varchar(100),kycVerificationStatus BOOLEAN,adharNumber varchar(12),panNumber varchar(10));
describe customers;
select * from customers;
alter table customers add account longtext;	
update customers set kycVerificationStatus = 1 where id = 355;

create table accounts (accountNumber long ,Holdername varchar(20),accountType varchar(20),bankName varchar(12),branchCode varchar(10),ifscCode varchar(10),balance double );

describe accounts;

alter table accounts add customerId int;

select * from accounts;

Update accounts set balance = 0.0 ,customerId = 355;

select balance,accountType from accounts where customerId = 222;

