package com.bank.manage.controllers;

import java.sql.SQLException;

import com.bank.manage.entites.Customer;
import com.bank.manage.exceptions.ProcessTerminationException;

public class BankOperations {
	
	public static void main(String[] args) throws SQLException, ProcessTerminationException, ClassNotFoundException {
		CustomerOperations cops = new CustomerOperations();
		AccountOperations aops = new AccountOperations();
		
		Customer c = new Customer();
	 //   c.setId("10002");
	
	
		//cops.removeCustomer(10004);
	    cops.registerCustomer(c);
		// System.out.println(cops.checkCustomerKyc(c));// 0 or 0<
	    
	   //System.out.println(aops.containsAccount_With_ID(c.getId()));  //check if any account associated with id
	   
	  //  aops.updateUserKYC(c.getId());
	 //  aops.createAccount(c);
	    
	    //cops.getUserNameById(c.getId());
	   // cops.getUserById(c.getId());
	     //aops.checkBalance(c);
	
	  //  aops.deposit(c, 1.0);
	    //aops.withdraw(c, 10000.0);
		//aops.closeAccount(c);
		//cops.deleteCustomer(c);
	}

}

