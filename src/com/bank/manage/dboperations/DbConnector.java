package com.bank.manage.dboperations;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnector {
	
static Connection connection;
	
	public static Connection createMyConnection()  {
		try {
			//load the driver 
			Class.forName("com.mysql.cj.jdbc.Driver");
			//create the connection 
			String url = "jdbc:mysql://localhost:3306/bank_system";
			String user = "root";
			String password = "root";
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
    
//	public static void main(String[] args) throws SQLException {
//	    createMyConnection();
//		Statement stmt = connection.createStatement();
//		ResultSet rs = stmt.executeQuery("select * from customers");
//		
//		while(rs.next()) {
//			System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getInt(4)+" "+rs.getString(5)+" "+rs.getInt(6)+" "+rs.getString(7)+" "+rs.getString(8)+" "+rs.getString(9));
//		}
//		
//		System.out.println( "sdfa");}
	
	
}
