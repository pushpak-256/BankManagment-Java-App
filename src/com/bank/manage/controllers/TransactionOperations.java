package com.bank.manage.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import com.bank.manage.Enums.TransactionType;
import com.bank.manage.dboperations.DbConnector;
import com.bank.manage.entites.Account;
import com.bank.manage.entites.Customer;
import com.bank.manage.entites.Transaction;

public class TransactionOperations {
	public void registerTransaction(Account a, Customer c, double amount, TransactionType type) throws SQLException {
		Transaction t = new Transaction();
		
        int transactionNumber=ThreadLocalRandom.current().nextInt();
        if (transactionNumber<0) {transactionNumber*=-1;}
        
        char transactionType= type.toString().toLowerCase().charAt(0);
        
        String generatedTransactionID = 
        		new StringBuilder().append(transactionType).append(transactionNumber).toString(); 
        
     	t.setId(generatedTransactionID);
		t.setAmount(amount);
		t.setSentByAccNO(a.getAccountNumber());
		t.setSentByName(a.getHolderName());
		t.setReceiverAccNO(a.getAccountNumber());
		t.setReceiverName(a.getHolderName());
		t.setTransactionType(type);
		t.setDate(LocalDateTime.now());

		String query = "insert into Transaction values(?,?,?,?,?,?,?,?)";
		Connection con = DbConnector.createMyConnection();
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, t.getId());
		pstmt.setDouble(2, t.getAmount());
		pstmt.setString(3, t.getSentByAccNO());
		pstmt.setString(4, t.getSentByName());
		pstmt.setString(5, t.getReceiverAccNO());
		pstmt.setString(6, t.getReceiverName());
		pstmt.setString(7, t.getTransactionType().name());

		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatDateTime = LocalDateTime.now().format(format);

		pstmt.setString(8, formatDateTime);

		int i = pstmt.executeUpdate();

		if (i <= 0) {
			System.out.println("Something went wrong");
		} else {
			System.out.println("Transaction inserted");
		}

	}

	public void getTransactionByLimit(String sentByAccNo) throws SQLException {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter total no of transaction you want to be displayed: ");
		int limit = sc.nextInt();
		String query = "SELECT * FROM (SELECT * FROM transaction ORDER BY date DESC LIMIT ?) sub ORDER BY sentByAccNo DESC";
		Connection con = DbConnector.createMyConnection();
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, limit);

		ResultSet rs = pstmt.executeQuery();
		if (rs != null) {

			while (rs.next()) {
				System.out.println(rs.getString(1) + " " + rs.getDouble(2) + " " + rs.getString(3) + " "
						+ rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " "
						+ rs.getString(8));
			}
		} else {
			System.out.println("System down!!");
		}
	}
}