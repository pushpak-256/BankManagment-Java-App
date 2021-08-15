package com.bank.manage.controllers;

import java.sql.SQLException;

import com.bank.manage.entites.Customer;

public class BankOperations {
	
	public static void main(String[] args) throws SQLException {
		CustomerOperations cops = new CustomerOperations();
		AccountOperations aopss = new AccountOperations();
		
		Customer c = new Customer();
	    c.setId("555");
	    cops.deleteCustomer(c);
		cops.registerCustomer(c);
		
		
	    c.setId("222");
	    cops.getUserById(c.getId());
	    aopss.checkBalance(c);
		aopss.createAccount(c);
	    aopss.deposit(c, 50000.0);
	    aopss.withdraw(c, 26000.0);
		aopss.closeAccount(c);
		
	}

}
