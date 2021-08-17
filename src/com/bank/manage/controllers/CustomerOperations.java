package com.bank.manage.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.bank.manage.dboperations.DbConnector;
import com.bank.manage.entites.Address;
import com.bank.manage.entites.Customer;
import com.bank.manage.exceptions.ProcessTerminationException;

public class CustomerOperations {
	Scanner sc = new Scanner(System.in);
	Connection con = DbConnector.createMyConnection();
	

	// register Customer in Bank without KYC and Account Creation
	public void registerCustomer(Customer c) throws SQLException {
		String query = "Select * from customers where id = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, c.getId());
		ResultSet rs = statement.executeQuery();
		boolean status = rs.next();
		try {
			if (status) {
				System.out.println("User with Specified Details Already Exists in Database");
			} else {
				System.out.println("Enter Id");
				String cid = sc.nextLine();
				System.out.println("Enter Name");
				String name = sc.nextLine();
				System.out.println("Enter Email");
				String email = sc.nextLine();
				System.out.println("Enter Phone");
				String phone = sc.nextLine();
				System.out.println("Enter Room or Flat Number");
				int rNUmber = sc.nextInt();
				System.out.println("Enter AddressLine 1");
				sc.nextLine();
				String adressLine1 = sc.nextLine();
				System.out.println("Enter AddressLine 2");
				String addressLine2 = sc.nextLine();
				System.out.println("Enter City");
				String city = sc.nextLine();
				System.out.println("Enter Country");
				String country = sc.nextLine();
				System.out.println("Enter PinCode");
				int code = sc.nextInt();
				System.out.println("Enter Addhar Number");
				sc.nextLine();
				String adhar = sc.nextLine();
				System.out.println("Enter Pan Number");
				String pan = sc.nextLine();
				Address a = new Address(rNUmber, adressLine1, addressLine2, city, country, code);

				PreparedStatement insertstatement = con
						.prepareStatement("insert into customers values(?,?,?,?,?,?,?,?,?)");
				insertstatement.setString(1, cid);
				insertstatement.setString(2, name);
				insertstatement.setString(3, email);
				insertstatement.setString(4, phone);
				insertstatement.setString(5, a.toString());
				insertstatement.setInt(6, 0);
				insertstatement.setString(7, adhar);
				insertstatement.setString(8, pan);
				insertstatement.setString(9, "Empty Account");

				int i = insertstatement.executeUpdate();

				if (i <= 0) {
					System.out.println("Something went Wrong");
				} else {
					System.out.println("Customer Registered Successfully");
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			con.close();
		}
	}

	// Delete Customer from database by ID
    public void deleteCustomer(Customer c) throws SQLException, ProcessTerminationException {
        String query = " select * from accounts where customerId = ?";
        con = DbConnector.createMyConnection();
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, c.getId());
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()) {
            throw new ProcessTerminationException("Cannot delete customer having active account");
        }
        else {
            String delquery = "delete from customers where id = ?";
            try {
                PreparedStatement stmt = con.prepareStatement(delquery);
                stmt.setString(1, c.getId());
                int res = stmt.executeUpdate();
                if (res <= 0) {
                    System.out.println("Something went Wrong");
                } else {
                    System.out.println("Customer Deleted Succssfully");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                con.close();
            }
        }

    }


	// Check Customer KYC Status
	public int checkCustomerKyc(Customer c) throws SQLException {
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement("select * from customers where id = ?");
			pstmt.setString(1, c.getId());
			ResultSet i = pstmt.executeQuery();
			if (i.next()) {
				int kycstatus = i.getInt(6);
				return kycstatus;
			} else {
				System.out.println("User Not Found");
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			con.close();
		}
		return 0;
	}

	// Get ResultSet Object For User from Database By Id
	public ResultSet getUserById(String id) throws SQLException {
		String query = "select * from customers where id = ?";
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			ResultSet res = pstmt.executeQuery();
			if (res.next()) {
				System.out.println("User Found");
				return res;
			} else {
				System.out.println("User Not Found");
				return null;
			}
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
		

	}

	public void updateCustomerTable(Customer c) throws SQLException {
		PreparedStatement insertstatement = con.prepareStatement("update customers set account = ? where id = ? ");
		insertstatement.setString(1, c.getAccount().toString());
		insertstatement.setString(2, c.getId());

		int res = insertstatement.executeUpdate();

		System.out.println(res + "Record Updated");
        con.close();
	}

}
