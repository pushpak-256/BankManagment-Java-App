package com.bank.manage.entites;

import java.sql.SQLException;

public interface AccountOps {
	
	void createAccount(Customer c) ;
	
	void closeAccount(Customer c);
	
	void deposit(Customer c,double amount);
	
	void withdraw(Customer c,double amount);
	
	void checkBalance(Customer c);

}
