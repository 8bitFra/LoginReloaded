package fra.plugin.login;

import java.sql.*;

public class CreateDatabase 
{
	public static void inizializeDatabase() 
	{
		String url = "jdbc:sqlite:plugins/LoginReloaded/database.db";
		
		String sql = "CREATE TABLE IF NOT EXISTS users (\n" 
				+ " uuid text PRIMARY KEY,\n" //not used - future update
                + " name text NOT NULL,\n"  
                + " register_ip text,\n" //not used - future update
                + " ip text,\n"  
                + " session_open boolean,\n" //not used - future update
                + " logged boolean,\n"  
                + " pwhash text\n"
                + ");";  
		
		try 
		{  
            Connection conn = DriverManager.getConnection(url);  
            if (conn != null) 
            {  
                DatabaseMetaData meta = conn.getMetaData();  
                System.out.println("The driver name is " + meta.getDriverName());  
                System.out.println("A new database has been created.");  
            }  
            
            PreparedStatement pstmt = conn.prepareStatement(sql); 
            
            pstmt.executeUpdate(); 
   
        } 
		catch (SQLException e) 
		{
            System.out.println(e.getMessage());  
        }  
		
	}
}
