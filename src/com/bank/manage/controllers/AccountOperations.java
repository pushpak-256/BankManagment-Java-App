package com.bank.manage.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import com.bank.manage.Enums.AccountType;
import com.bank.manage.Enums.TransactionType;
import com.bank.manage.dboperations.DbConnector;
import com.bank.manage.entites.Account;
import com.bank.manage.entites.AccountOps;
import com.bank.manage.entites.Customer;
import com.bank.manage.exceptions.LowBalanceException;
import com.bank.manage.exceptions.NegativeBalanceException;
import com.bank.manage.exceptions.ProcessTerminationException;
import com.mysql.cj.exceptions.RSAException;

public class AccountOperations implements AccountOps {

	Scanner sc = new Scanner(System.in);
	Connection con = DbConnector.createMyConnection();
	CustomerOperations cops = new CustomerOperations();
	TransactionOperations t = new TransactionOperations();
 //   AccountOperations aops = new AccountOperations();
    
	/*
	 * checks kYC Status with custID Does not allow creation of account if KYC is
	 * pending Creates Account if KYC is Done And Updates Customer Table
	 */
	@Override
	public void createAccount(Customer c) {
	try
		{
		    if(cops.checkCustomerKyc(c)!=0) 
			{
		    	AccountOperations aops = new AccountOperations();
		    	String accountNum=aops.containsAccount_With_ID(c.getId());
		    	
	    	   if ( accountNum !=null )
	    	   {
	    		   System.err.println
	    		   ("User with id "+c.getId()+"\nalready has account with account number "+accountNum);
			   } 
	    	   
	    	   else 
	    	   {
	    		   
		    	System.out.println("Select 1 for Opening Savings Account or 2 for Current Account");
				int accountchoice = sc.nextInt();
				
				Account ac = new Account();
				
			//account number
				long accountNumberGenerated = ThreadLocalRandom.current().nextLong();
				if(accountNumberGenerated<0) {accountNumberGenerated*=-1;}
				
				ac.setAccountNumber(Long.toString(accountNumberGenerated));
			
			//account holder name
				String name = cops.getUserNameById(c.getId());
				ac.setHolderName(name);
				
			//default bank details
				ac.setBankName("SBI");
				ac.setBranch_code("1025");
				ac.setIfsc_code("SBIN0125");
				
				if (accountchoice == 1) {
					ac.setAccountType(AccountType.SAVINGS);
					ac.setBalance(1000);
				} 
				
				else
				{
					ac.setAccountType(AccountType.CURRENT);
					ac.setBalance(5000.0);
				}
				
				c.setAccount(ac);
			
				//updating Accounts table
				//AccountOperations aops = new AccountOperations();
		    	aops.updateAccountsTable(c);}
			}
		    
		    else
			{
				System.err.println("Please Complete Your KYC first");
                throw new ProcessTerminationException("KYC is not verified");			
			}
		} 
	catch (SQLException | ProcessTerminationException e) 
		{
		// e.getStackTrace();
		 e.getMessage();
		} 
		
	
		
	}
	//		try {
//			int id = Integer.parseInt(c.getId()) ;
//			ResultSet resultset = cops.getUserById(c.getId());
//			
//			if (resultset != null) {
//				int kycstatus = resultset.getInt(6);
//				
//		
//		 		
//	           
//            PreparedStatement stmt =
//            		con.prepareStatement("select * from accounts where id=?");
//            stmt.setInt(1, id); 
//           ResultSet i = stmt.executeQuery();
//           System.out.println(i+" int is ");
//           
//				if (kycstatus != 0) {
//				//	ResultSet resultset1 = cops.getA(c.getId());
//				} 
//				else {
//					
//					System.out.println("Please update KYC status ");
//				}
//				
//			} 
//			
//			else {	System.out.println("User Not Found");
//			
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

	/*
	 * Deposit Amount to customer Account and Display it
	 */
	@Override
	public void deposit(Customer c, double amount) {
		try {
			ResultSet i = cops.getUserById(c.getId());
			int kycstatus = i.getInt(6);
			System.out.println(kycstatus);
			
			 if (kycstatus == 0)
			{
				System.out.println("Kyc Not Updated");
				throw new ProcessTerminationException("Process Terminated");
			}
			 
			 else
			{
				String query = "select balance from accounts where id = ?";
				PreparedStatement pst = con.prepareStatement(query);
				pst.setString(1, c.getId());
				ResultSet s = pst.executeQuery();
				s.next();
				
				double bal = s.getDouble(1);
				double finalamt = bal + amount;
				
				if (finalamt > 0.0) 
				{
					System.out.println
					("Deposited amount : " + amount + " and Available Balance is : " + finalamt);
					
					AccountOperations aops = new AccountOperations();
					aops.updateBalanceInAccount(finalamt, c);
					
					ResultSet rs = getAccountById(c.getId());
					
					 if (rs != null)
					{
						Account ac = new Account();
						ac.setAccountNumber(rs.getString(1));
						ac.setHolderName(rs.getString(2));
						t.registerTransaction(ac, c, amount, TransactionType.Deposit);
					}
					 
					 else 
						{System.out.println("Balance Updated but transaction entry failed");}
					
				}
				
				else {
					try {
						throw new NegativeBalanceException("Negative Balance");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException | ProcessTerminationException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Withdraw Amount from customer Account and Display Available Balance
	 */
	@Override
	public void withdraw(Customer c, double amount) {
		System.out.println("Coming inside withdraw");
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from customers where id = ?");
			pstmt.setString(1, c.getId());
			ResultSet i = pstmt.executeQuery();
			if (i.next()) {
				int kycstatus = i.getInt(6);
				System.out.println(kycstatus);
				if (kycstatus == 0) {
					System.out.println("Kyc Not Updated");
					throw new ProcessTerminationException("Process Terminated");
				} else {
					String query = "select balance,accountType from accounts where id = ?";
					PreparedStatement pst = con.prepareStatement(query);
					pst.setString(1, c.getId());
					ResultSet s = pst.executeQuery();
					s.next();
					double bal = s.getDouble(1);
					String type = s.getString(2);
					if (bal > 0) {
						if (type.equals("SAVINGS")) {
							if (bal < 1000) {
								throw new LowBalanceException("Minimum Balance Should be 1000");
							} else {
								double balafterwithdrawal = bal - amount;
								if (balafterwithdrawal < 1000) {
									throw new LowBalanceException("Minimum Balance Should be 1000");
								} else {
									AccountOperations aops = new AccountOperations();
									aops.updateBalanceInAccount(balafterwithdrawal, c);
									System.out.println("WithDrawn amount : " + amount + " and Available Balance is : "
											+ balafterwithdrawal);
									ResultSet rs = getAccountById(c.getId());
									if (rs != null) {
										Account ac = new Account();
										ac.setAccountNumber(rs.getString(1));
										ac.setHolderName(rs.getString(2));
										t.registerTransaction(ac, c, amount, TransactionType.Withdrawn);
									} else {
										System.out.println("Balance Updated but transaction entry failed");
									}
								}
							}
						} else {
							if (type.equals("CURRENT")) {
								if (bal < 5000) {
									throw new LowBalanceException("Minimum Balance Should be 5000");
								} else {
									double balafterwithdrawal = bal - amount;
									if (balafterwithdrawal < 5000) {
										throw new LowBalanceException("Minimum Balance Should be 5000");
									} else {
										AccountOperations aops = new AccountOperations();
										aops.updateBalanceInAccount(balafterwithdrawal, c);
										System.out.println("WithDrawn amount : " + amount
												+ " and Available Balance is : " + balafterwithdrawal);
										ResultSet rs = getAccountById(c.getId());

										if (rs != null) {
											Account ac = new Account();
											ac.setAccountNumber(rs.getString(1));
											ac.setHolderName(rs.getString(2));
											t.registerTransaction(ac, c, amount, TransactionType.Withdrawn);
										} else {
											System.out.println("Balance Updated but transaction entry failed");
										}
									}
								}
							}

						}
					} else {
						try {
							throw new NegativeBalanceException("Negative Balance");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			} else {
				System.out.println("Something went Wrong");
			}
		} catch (SQLException | ProcessTerminationException | LowBalanceException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Retrieves Balance From customer Account and Display it
	 */
	@Override
	public void checkBalance(Customer c) {
		try {
			ResultSet i = cops.getUserById(c.getId());
			int kycstatus = i.getInt(6);
			
			if (kycstatus == 0) {
				System.out.println("Kyc Not Updated");
				throw new ProcessTerminationException("Process Terminated");
			}
			
			else
			{	
				String query = "select balance from accounts where id = ?";
				PreparedStatement pst = con.prepareStatement(query);
				pst.setString(1, c.getId());
				ResultSet s = pst.executeQuery();
				s.next();
				double bal = s.getDouble(1);
				
				if (bal > 0) 
				{ System.out.println("Available Balance is : " + bal);}
				
				else 
				{
					try 
					{throw new NegativeBalanceException("Negative Balance");}
					
					catch (Exception e) {e.printStackTrace();}
				}

			}
		} catch (SQLException | ProcessTerminationException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Deletes the Account Record from the Customer (Not Working)
	 */
	@Override
	public void closeAccount(Customer c) {
		String query = "Select * from accounts where id = ?";
		try {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, c.getId());
			ResultSet set = stmt.executeQuery();
			set.next();
			double bal = set.getDouble(7);
			if (bal > 0) {
				System.out.println("Balance Should be 0 before Closing the Account");
				throw new ProcessTerminationException("Please Debit All the Balance Amount From Account");
			} else {
				String deletequery = "Delete from accounts where id = ?";
				PreparedStatement pstmt = con.prepareStatement(deletequery);
				pstmt.setString(1, c.getId());
				int result = pstmt.executeUpdate();
				if (result > 0) {
					System.out.println("Account Deleted");
				} else {
					System.out.println("Something Went Wrong");
				}
			}
		} catch (SQLException | ProcessTerminationException e) {

			e.printStackTrace();
		}

	}

	// update user KYc
	public void updateUserKYC(String id) throws SQLException, ClassNotFoundException {
		ResultSet s = cops.getUserById(id);
		try {
			if (s != null) {
				String query = "update customers set kycVerificationStatus =? where id = ?";
				con = DbConnector.createMyConnection();
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.setInt(1, 1);
				pstmt.setString(2, id);
				int result = pstmt.executeUpdate();
				if (result > 0) {
					System.out.println("KYC Updated successfully");
				} else {
					System.out.println("Something Went Wrong");
				}
			} else {
				System.err.println("User Not Found");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	// Insert Record into Account Table
	public void updateAccountsTable(Customer c) {

		String query = "insert into accounts values(?,?,?,?,?,?,?,?)";
		try {
			Connection con2 = DbConnector.createMyConnection();
			PreparedStatement pstmt = con2.prepareStatement(query);
			pstmt.setString(1, c.getAccount().getAccountNumber());
			pstmt.setString(2, c.getAccount().getHolderName());
			pstmt.setString(3, c.getAccount().getAccountType().name());
			pstmt.setString(4, c.getAccount().getBankName());
			pstmt.setString(5, c.getAccount().getBranch_code());
			pstmt.setString(6, c.getAccount().getIfsc_code());
			pstmt.setDouble(7, c.getAccount().getBalance());
			pstmt.setString(8, c.getId());

			int recordaffected = pstmt.executeUpdate();

			if (recordaffected > 0) {
				AccountOperations aops = new AccountOperations();
				System.out.println
				("Account Added \nwith Account Number "+aops.containsAccount_With_ID(c.getId()));
			} else {
				System.out.println("Something Went Wrong");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateBalanceInAccount(double balance, Customer c) {
		String query = "update accounts set balance = ? where id = ? ";
		try {
			Connection con2 = DbConnector.createMyConnection();
			PreparedStatement pstmt = con2.prepareStatement(query);
			pstmt.setDouble(1, balance);
			pstmt.setString(2, c.getId());

			int recordaffected = pstmt.executeUpdate();

			System.out.println(recordaffected);
			if (recordaffected > 0) {
				System.out.println("Balance Updated");
			} else {
				System.out.println("Something Went Wrong");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//not needed this method anymore
	public ResultSet getAccountById(String id) throws SQLException {
		String query = "select * from accounts where id = ?";
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			ResultSet res = pstmt.executeQuery();
			if (res.next()) {
				System.out.println("Account Found");
				return res;
			} else {
				System.out.println("Account Not Found");
				return null;
			}
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	public String containsAccount_With_ID(String id) throws SQLException {
		
		String query = "select accountNumber from accounts where id = ?";
		
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			ResultSet res = pstmt.executeQuery();
			
			if (res.next())
			{return res.getString(1);} 
			else {
			
				return null;
			}
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}

}
