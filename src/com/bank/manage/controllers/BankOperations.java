package com.bank.manage.controllers;

import java.sql.SQLException;

import com.bank.manage.entites.Customer;
import com.bank.manage.exceptions.ProcessTerminationException;

public class BankOperations {
	
	public static void main(String[] args) throws SQLException, ProcessTerminationException, ClassNotFoundException {
		CustomerOperations cops = new CustomerOperations();
		AccountOperations aopss = new AccountOperations();
		
		Customer c = new Customer();
	   // c.setId("1");
	    cops.registerCustomer(c);
	    //aopss.updateUserKYC(c.getId());
		aopss.createAccount(c);
	    
	    //cops.getUserById(c.getId());
	    //aopss.checkBalance(c);
	
	    //aopss.deposit(c, 10000.0);
	    //aopss.withdraw(c, 10000.0);
		//aopss.closeAccount(c);
		//cops.deleteCustomer(c);
	}

}
