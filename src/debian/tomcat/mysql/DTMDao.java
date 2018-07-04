package debian.tomcat.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DTMDao {
	
   static final String driver = "com.mysql.cj.jdbc.Driver";
   static final String url = "jdbc:mysql://172.16.23.188:3306/testDB?useLegacyDatetimeCode=false&serverTimezone=UTC";
   static final String user = "dtm";
   static final String pass = "dtmpass";
   static Connection conn = null;
   static PreparedStatement preparedStatement = null;
   static String sql = "";
   static int numUpd = 0;
	
	public DTMDao() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pass);
			if(!conn.isClosed())
				System.out.println("connecting to the database successfully!");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	//Create
	public boolean testCreate(testData td)
	{
		numUpd = 0;
		sql = "INSERT INTO testTable (name, value) VALUES (?,?)";		
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, td.name);
			preparedStatement.setString(2, td.value);
			numUpd = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numUpd>0?true:false;
	}
	
	//Retrieve
	public ResultSet testRetrieve()
	{
		sql = "SELECT * FROM testTable";
		try {
			preparedStatement = conn.prepareStatement(sql);
			return preparedStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//Update
	public boolean testUpdate(testData td)
	{
		numUpd = 0;
		sql = "UPDATE testTable SET name=?, value=? WHERE id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, td.name);
			preparedStatement.setString(2, td.value);
			preparedStatement.setInt(3, td.id);
			numUpd = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numUpd>0?true:false;
	}
	
	//Delete
	public boolean testDelete(testData td)
	{
		numUpd = 0;
		sql = "DELETE FROM testTable WHERE id=?";
		try {
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, td.id);
			numUpd = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numUpd>0?true:false;
	}
}