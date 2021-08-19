package com.bank.manage.controllers;

import java.sql.SQLException;

import com.bank.manage.entites.Customer;
import com.bank.manage.entites.Transaction;
import com.bank.manage.exceptions.ProcessTerminationException;

public class BankOperations {
	
	public static void main(String[] args) throws SQLException, ProcessTerminationException, ClassNotFoundException {
		CustomerOperations cops = new CustomerOperations();
		AccountOperations aops = new AccountOperations();
		 TransactionOperations t = new TransactionOperations();
		 
		Customer c = new Customer();
	    c.setId("9999");
	
	
		//cops.removeCustomer(10004);
	   // cops.registerCustomer(c);
		// System.out.println(cops.checkCustomerKyc(c));// 0 or 0<
	    
	   //System.out.println(aops.containsAccount_With_ID(c.getId()));  //check if any account associated with id
	   
//	    aops.updateUserKYC(c.getId());
//	  aops.createAccount(c);
	    
	    //cops.getUserNameById(c.getId());
	   // cops.getUserById(c.getId());
	  //  aops.checkBalance(c);
	
	    //aops.deposit(c,  340);
	    //aops.withdraw(c,-124321);
	   
	    //t.getTransactionByLimit("9999");  // pass  id as parameter 
	    
		//aops.closeAccount(c);
		cops.deleteCustomer(c);
	}

}

