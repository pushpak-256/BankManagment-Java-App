package com.bank.manage.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import com.bank.manage.Enums.AccountType;
import com.bank.manage.dboperations.DbConnector;
import com.bank.manage.entites.Account;
import com.bank.manage.entites.AccountOps;
import com.bank.manage.entites.Customer;
import com.bank.manage.exceptions.LowBalanceException;
import com.bank.manage.exceptions.NegativeBalanceException;
import com.bank.manage.exceptions.ProcessTerminationException;

public class AccountOperations implements AccountOps {

	Scanner sc = new Scanner(System.in);
	Connection con = DbConnector.createMyConnection();
	CustomerOperations cops = new CustomerOperations();
	

	/*
	 * checks kYC Status with custID Does not allow creation of account if KYC is
	 * pending Creates Account if KYC is Done And Updates Customer Table
	 */
	@Override
	public void createAccount(Customer c) {
		try {
			ResultSet i = cops.getUserById(c.getId());
			if (i != null) {
				int kycstatus = i.getInt(6);
				System.out.println("@@@@@" + kycstatus);
				if (kycstatus != 0) {
					System.out.println("Select 1 for Opening Savings Account or 2 for Current Account");
					int accountchoice = sc.nextInt();
					Account ac = new Account();
					ac.setAccountNumber(Integer.toString(ThreadLocalRandom.current().nextInt()));
					String name = i.getString(2);
					ac.setHolderName(name);
					ac.setBankName("SBI");
					ac.setBranch_code("1025");
					ac.setIfsc_code("SBIN0125");
					if (accountchoice == 1) {
						ac.setAccountType(AccountType.SAVINGS);
						ac.setBalance(1000);
					} else {
						ac.setAccountType(AccountType.CURRENT);
						ac.setBalance(5000.0);
					}
					c.setAccount(ac);

					cops.updateCustomerTable(c);
					AccountOperations aops = new AccountOperations();
					aops.updateAccountsTable(c);
					
				} else {
					System.out.println("System Down");
				}
			} else {
				System.out.println("User Not Found");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Deposit Amount to customer Account and Display it
	 */
	@Override
	public void deposit(Customer c, double amount) {

		try {
			ResultSet i = cops.getUserById(c.getId());
			int kycstatus = i.getInt(6);
			System.out.println(kycstatus);
			if (kycstatus == 0) {
				System.out.println("Kyc Not Updated");
				throw new ProcessTerminationException("Process Terminated");
			} else {
				String query = "select balance from accounts where customerId = ?";
				PreparedStatement pst = con.prepareStatement(query);
				pst.setString(1, c.getId());
				ResultSet s = pst.executeQuery();
				s.next();
				double bal = s.getDouble(1);
				double finalamt = bal + amount;
				if (finalamt > 0.0) {
					System.out.println("Deposited amount : " + amount + " and Available Balance is : " + finalamt);
					AccountOperations aops = new AccountOperations();
					aops.updateBalanceInAccount(finalamt, c);
				} else {
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
					String query = "select balance,accountType from accounts where customerId = ?";
					PreparedStatement pst = con.prepareStatement(query);
					pst.setString(1, c.getId());
					ResultSet s = pst.executeQuery();
					s.next();
					double bal = s.getDouble(1);
					String type = s.getString(2);
					System.out.println(bal);
					System.out.println(type);
					if (bal > 0) {
						if (type.equals("SAVINGS")) {
							if (bal < 1000) {
								throw new LowBalanceException("Minimum Balance Should be 1000");
							} else {
								double balafterwithdrawal = bal - amount;
								System.out.println(balafterwithdrawal);
								if (balafterwithdrawal < 1000) {
									throw new LowBalanceException("Minimum Balance Should be 1000");
								} else {
									AccountOperations aops = new AccountOperations();
									aops.updateBalanceInAccount(balafterwithdrawal, c);
									System.out.println("WithDrawn amount : " + amount + " and Available Balance is : "
											+ balafterwithdrawal);
								}
							}
						} else {
							if (type.equals("CURRENT")) {
								if (bal < 5000) {
									throw new LowBalanceException("Minimum Balance Should be 5000");
								} else {
									double balafterwithdrawal = c.getAccount().getBalance() - amount;
									if (balafterwithdrawal < 5000) {
										throw new LowBalanceException("Minimum Balance Should be 5000");
									} else {
										AccountOperations aops = new AccountOperations();
										aops.updateBalanceInAccount(balafterwithdrawal, c);
										System.out.println("WithDrawn amount : " + amount
												+ " and Available Balance is : " + balafterwithdrawal);
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
			System.out.println(kycstatus);
			if (kycstatus == 0) {
				System.out.println("Kyc Not Updated");
				throw new ProcessTerminationException("Process Terminated");
			} else {
				String query = "select balance from accounts where customerId = ?";
				PreparedStatement pst = con.prepareStatement(query);
				pst.setString(1, c.getId());
				ResultSet s = pst.executeQuery();
				s.next();
				double bal = s.getDouble(1);
				if (bal > 0) {
					System.out.println("Available Balance is : " + bal);
				} else {
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
	 * Deletes the Account Record from the Customer (Not Working)
	 */
	@Override
	public void closeAccount(Customer c) {
		String query = "Select * from accounts where customerId = ?";
		try {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1,c.getId());
			ResultSet set =  stmt.executeQuery();
			set.next();
			double bal = set.getDouble(7);
			if (bal > 0) {
				System.out.println("Balance Should be 0 before Closing the Account");
				throw new ProcessTerminationException("Please Debit All the Balance Amount From Account");
			} else {
				String deletequery = "Delete from accounts where customerId = ?";
				PreparedStatement pstmt = con.prepareStatement(deletequery);
				pstmt.setString(1,c.getId());
				int result =  pstmt.executeUpdate();
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
	public void updateUserKYC(String id) throws SQLException {
		ResultSet s = cops.getUserById(id);
		try {
			if (s != null && s.next()) {
				String query = "Update customer set kycVerificationStatus = ? where id = ?";
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.setInt(1, 1);
				pstmt.setString(2, id);
				int result = pstmt.executeUpdate();
				if (result > 0) {
					System.out.println(result + " Record Updated");
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
    
	//Insert Record into Account Table
	public void updateAccountsTable(Customer c) {
		System.out.println("Came inside");
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
			
			System.out.println(recordaffected);
			if (recordaffected > 0) {
				System.out.println("Account Added");
			} else {
				System.out.println("Something Went Wrong");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void updateBalanceInAccount(double balance,Customer c) {
		String query = "update accounts set balance = ? where customerId = ? ";
		try {
			Connection con2 = DbConnector.createMyConnection();
			PreparedStatement pstmt = con2.prepareStatement(query);
			pstmt.setDouble(1,  balance);
			pstmt.setString(2, c.getId());
			
			
			int recordaffected = pstmt.executeUpdate();
			
			System.out.println(recordaffected);
			if (recordaffected > 0) {
				System.out.println("Balance Updated");
			} else {
				System.out.println("Something Went Wrong");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
