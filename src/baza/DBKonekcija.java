package baza;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBKonekcija {

	private static final String USERNAME = "";
	private static final String PASSWORD = "";
	private static final String M_CONN_STRING =
			"jdbc:mysql://localhost/Administrator";
	
	private static Connection con=null;
	private static ResultSet rs=null;
	
	public static Connection getCon() {
		return con;
	}

	public static void setCon(Connection con) {
		DBKonekcija.con = con;
	}

	public static ResultSet getRs() {
		return rs;
	}

	public static void setRs(ResultSet rs) {
		DBKonekcija.rs = rs;
	}

	public static Connection getConnection() {
		
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(M_CONN_STRING, USERNAME, PASSWORD);
		} catch (SQLException e) {
			System.err.println("SQLException in getConnection, " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundExceprion in getConnection, " + e.getMessage());
		} 
		
		return con;
	}
	
	public void closeCon() {
		
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("nijw se zatvorila konekcija " + e.getMessage());
		}
	}
	


	public static ResultSet getResultS(String sql_str) {
	
		try {
			Statement stmt=con.createStatement();
			rs=stmt.executeQuery(sql_str);
		} catch (SQLException e) {
			System.err.println("SQLException in getResultSet, " + e.getMessage());
		}
		
		return rs;
	}
	
	

}
