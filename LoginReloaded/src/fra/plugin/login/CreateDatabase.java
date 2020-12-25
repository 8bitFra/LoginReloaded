package fra.plugin.login;

import java.sql.*;

public class CreateDatabase 
{
	public static String tabella_utenti = "CREATE TABLE IF NOT EXISTS users (\n" 
			+ " uuid text PRIMARY KEY,\n" //not used - future update
            + " name text NOT NULL,\n"  
            + " register_ip text,\n" //not used - future update
            + " ip text,\n"  
            + " session_open boolean,\n" //not used - future update
            + " logged boolean,\n"  
            + " pwhash text\n"
            + ");";  
	public static String tabella_ips = "CREATE TABLE IF NOT EXISTS ips (\n"
            + " ip text PRIMARY KEY,\n"
            + " amount integer\n"
            + ");";
	
	public static String url = "jdbc:sqlite:plugins/LoginReloaded/database.db";
	
	public static void inizializeDatabase() 
	{
		
		
		try 
		{  
            Connection conn = DriverManager.getConnection(url);  
            if (conn != null) 
            {  
                DatabaseMetaData meta = conn.getMetaData();  
                System.out.println("The driver name is " + meta.getDriverName());  
                System.out.println("A new database has been created.");  
            }
            
            CreateDatabase.insert(tabella_utenti);
            CreateDatabase.insert(tabella_ips);
            
            
        } 
		catch (SQLException e) 
		{
            System.out.println(e.getMessage());  
        }  
		
	}
	
	public static void checkifdbupdated()
	{
		CreateDatabase.insert(tabella_ips);
	}
	
	public static void insert(String query)
	{
		try
		{
			Connection conn = DriverManager.getConnection(url);
				
			PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
        } 
		catch (SQLException e) 
		{
            System.out.println(e.getMessage());
        }
	}
}
