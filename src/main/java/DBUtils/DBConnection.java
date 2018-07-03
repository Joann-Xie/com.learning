package DBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constants.Constants;

public class DBConnection {
	private static Connection con;
	private Statement stmt;
	public static DBConnection inst;
	
	public static DBConnection getInstance() throws SQLException{
		if(null == inst){
			inst = new DBConnection();
		}
		return inst;
	}
	
	private DBConnection() throws SQLException{
		try{
    		Class.forName("com.mysql.jdbc.Driver");
    		String uri = "jdbc:mysql://localhost:3306/" + Constants.DATABASE + "?autoReconnect=true&useSSL=false&";
    		con = DriverManager.getConnection(uri, Constants.DB_USERNAME, Constants.DB_PASSWD);
    		stmt = con.createStatement();
    	}catch(ClassNotFoundException e){
    		e.printStackTrace();
    	}catch(SQLException e){
    		e.printStackTrace();
    		throw e;
    	}
	}
	
    public void close() throws SQLException{
    	try{
    		con.close();
    	}catch(SQLException e){
    		e.printStackTrace();
    		throw e;
    	}
    }
    
    public ResultSet query(String sqlcommand) throws SQLException{
    	ResultSet rs;
    	try{
    	    rs = stmt.executeQuery(sqlcommand);
    	}catch(SQLException e){
    		e.printStackTrace();
    		throw e;
    	}
    	return rs;
    }
    public int update(String sqlcommand) throws SQLException{
    	int result;
    	try{
    		result = stmt.executeUpdate(sqlcommand);
    	}catch(SQLException e){
    		e.printStackTrace();
    		throw e;
    	}
    	return result;
    }
}
