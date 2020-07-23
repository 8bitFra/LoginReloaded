package fra.plugin.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import javax.xml.bind.DatatypeConverter;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.*;

public class Handler
{
	private static String url = "jdbc:sqlite:plugins/LoginReloaded/database.db";
	private static Connection conn;
	
	static
	{
		try 
		{
			conn = DriverManager.getConnection(url);
		} 
		catch (SQLException e) 
		{
			System.out.println("[ERROR] LoginReloaded - Can't connect to database.");
			e.printStackTrace();
		}
	}
	
	public static void closeSqlConn()
	{
		try 
		{
			conn.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
    public static void RegisterPlayer(final Player p, final String pw) {
    	
    	MessageDigest md;
		try 
		{
			md = MessageDigest.getInstance("MD5");
		} 
		catch (NoSuchAlgorithmException e) 
		{
			md = null;
		} 
		
		
		md.update(pw.getBytes());
	    byte[] digest = md.digest();
	    String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        
        
        
        String sql = "INSERT INTO users (uuid, name, pwhash) VALUES(?,?,?)";
        
        try
        {   
            PreparedStatement pstmt = conn.prepareStatement(sql);  
            pstmt.setString(1, p.getUniqueId().toString());
            pstmt.setString(2, p.getName());  
            pstmt.setString(3, myHash);  
            pstmt.executeUpdate();
            pstmt.close();
        } 
        catch (SQLException e) 
        {  
        	p.sendMessage("§cERROR: §7[E1]");
            System.out.println("[ERROR] LoginReloaded - Player data cant be save.");
        }
    }
    
    public static void addEffects(final Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 5));
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 5));
    }
    
    public static void removeEffects(final Player p) {
        p.removePotionEffect(PotionEffectType.BLINDNESS);
        p.removePotionEffect(PotionEffectType.INVISIBILITY);
    }
    
    public static void SucessfullLogin(final Player p) {
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5.0f, 12.0f);
    }
    
    public static void WrongLogin(final Player p) {
        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 5.0f, 12.0f);
    }
    
    public static void RegisterNewIP(final Player p, final String address) {     
        String sql = "UPDATE users SET ip = '"+ address +"' WHERE name = '"+p.getName()+"'";
        
        try
        {  
            Statement stmt = conn.createStatement();   
            stmt.execute(sql);  
            stmt.close();
        } 
        catch (SQLException e) 
        {  
        	p.sendMessage("§cERROR: §7Es [E2]");
            System.out.println("[ERROR] LoginReloaded - IP Database cant be save.");
        }
    }
    
    public static void ChangePassword(final Player p, final String pw) {
        
        MessageDigest md;
		try 
		{
			md = MessageDigest.getInstance("MD5");
		} 
		catch (NoSuchAlgorithmException e) 
		{
			md = null;
		} 
		
		
		md.update(pw.getBytes());
	    byte[] digest = md.digest();
	    String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        
        String sql = "UPDATE users SET pwhash = '"+ myHash +"' WHERE name = '"+p.getName()+"'";
        
        try
        {   
            Statement stmt = conn.createStatement();   
            stmt.execute(sql);  
            stmt.close();
        } 
        catch (SQLException e) 
        {  
        	p.sendMessage("§cERROR: §7[E3]");
        	e.printStackTrace();
            System.out.println("[ERROR] LoginReloaded - Player data cant be save.");
        }
    }
    
    public static void ResetPassword(final Player p)
    {	
    	String sql = "DELETE FROM users WHERE name = '"+p.getName()+"'";
        
        try
        {  
            Statement stmt = conn.createStatement();   
            stmt.execute(sql);  
            stmt.close();
        } 
        catch (SQLException e) 
        {  
        	p.sendMessage("§cERROR: §7[E4]");
            System.out.println("[ERROR] LoginReloaded - Player data cant be save.");
        }
    	
    	
    	LogoutCommand(p);
    }
    
    public static void RefreshIPList() {
    	String sql = "UPDATE users SET ip = null";
        
        try
        {  
            Statement stmt = conn.createStatement();   
            stmt.execute(sql);  
            stmt.close();
        } 
        catch (SQLException e) 
        {  
        	e.printStackTrace();
        }
    }
    
    public static void Login(final Player p) {       
        String sql = "UPDATE users SET logged = true WHERE name = '"+p.getName()+"'";
        
        try
        {  
            Statement stmt = conn.createStatement();   
            stmt.execute(sql);  
            stmt.close();
        } 
        catch (SQLException e) 
        {  
        	p.sendMessage("§cERROR: §7[E5]");
            System.out.println("[ERROR] LoginReloaded - Player status cant be save.");
        }
    }
    
    public static void Logout(final Player p) {
    	String sql = "UPDATE users SET logged = false WHERE name = '"+p.getName()+"'";
        
        try
        {   
            Statement stmt = conn.createStatement();   
            stmt.execute(sql);  
            stmt.close();
        } 
        catch (SQLException e) 
        {  
        	p.sendMessage("§cERROR: §7[E6]");
            System.out.println("[ERROR] LoginReloaded - Player status cant be save.");
        }
    }
    
    public static void LogoutCommand(final Player p)
    {
    	final String Login = Listeners.main.getConfig().getString("Messages.Login").replace("&", "§");
        final String Register = Listeners.main.getConfig().getString("Messages.Register").replace("&", "§");
        if (Handler.isRegistered(p.getName())) 
        {
        	Handler.Logout(p);
        	Handler.removeIp(p);
        	
            Handler.addEffects(p);
            Listeners.main.addSaveList(p);
            p.sendMessage(" ");
            p.sendMessage(Login);
            p.sendMessage(" ");
            
        }
        else 
        {
            Handler.addEffects(p);
            Listeners.main.addSaveList(p);
            p.sendMessage(" ");
            p.sendMessage(Register);
            p.sendMessage(" ");
        }
    }
    
    public static void removeIp(final Player p)
    {
    	String sql = "UPDATE users SET ip = null WHERE name = '"+p.getName()+"'";
        
        try
        {  
            Statement stmt = conn.createStatement();   
            stmt.execute(sql);  
            stmt.close();
        } 
        catch (SQLException e) 
        {  
        	p.sendMessage("§cERROR: §7Es [E2]");
            System.out.println("[ERROR] LoginReloaded - IP Database cant be save.");
        }
    }
    
    public static boolean isLoggedIn(String player) {
    	String sql = "SELECT logged from users WHERE name = '"+player+"'";
        
        try
        {  
            Statement stmt = conn.createStatement();   
            ResultSet rs = stmt.executeQuery(sql); 
            
            while (rs.next()) 
            {
            	if(rs.getBoolean("logged")) return true;
            }
            
            stmt.close();

        } 
        catch (SQLException e) 
        {  
        	e.printStackTrace();
        }
    	
    	
        return false;
    }
    
    public static boolean isOnline(String player)
    {  

        if(Bukkit.getServer().getPlayer(player) != null) return true;
        else return false;
        
    }
    
    public static boolean isRegistered(String player) {
    	String sql = "SELECT pwhash from users WHERE name = '"+player+"'";
        
    	boolean app = false;
    	
    	
        try
        {  
            Statement stmt = conn.createStatement();   
            ResultSet rs = stmt.executeQuery(sql); 
            
            while (rs.next())
            {
            	if(rs.getString("pwhash")!=null) app = true;
            }
            stmt.close();
        } 
        catch (SQLException e) 
        {  
        	e.printStackTrace();
        }
    	
        return app;
    }
    
    public static String getIpAddress(String player)
    {
    	String sql = "SELECT ip from users WHERE name = '"+player+"'";
        
        try
        {    
            Statement stmt = conn.createStatement();   
            ResultSet rs = stmt.executeQuery(sql); 
            
            while (rs.next()) 
            {  
            	return rs.getString("ip");
            }
            
            stmt.close();
        } 
        catch (SQLException e) 
        {  
        	e.printStackTrace();
        }
        
        return null;
    }
    
    public static String getMD5Password(String player)
    {
    	String sql = "SELECT pwhash from users WHERE name = '"+player+"'";
        
        try
        {    
            Statement stmt = conn.createStatement();   
            ResultSet rs = stmt.executeQuery(sql); 
            
            while (rs.next()) 
            {  
            	return rs.getString("pwhash");
            }
            
            stmt.close();
        } 
        catch (SQLException e) 
        {  
        	e.printStackTrace();
        }
        
        return null;
    }
    
    public static boolean isIPSave(final Player p, final String address1) {
    	String sql = "SELECT ip from users WHERE name = '"+p.getName()+"'";
        String address2 = null;
    	
        try
        {   
            Statement stmt = conn.createStatement();   
            ResultSet rs = stmt.executeQuery(sql); 
            
            while (rs.next()) {  
            	address2 = rs.getString("ip");
            }
            stmt.close();
        } 
        catch (SQLException e) 
        {  
        	e.printStackTrace();
        }
        return address1.equals(address2);
    }
    
    public static boolean PasswordMatch(final Player p, final String str) 
    {
    	String sql = "SELECT pwhash from users WHERE name = '"+p.getName()+"'";
        String pw = null;
    	
        try
        {  
            Statement stmt = conn.createStatement();   
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {  
            	pw = rs.getString("pwhash");
            }
            stmt.close();
        } 
        catch (SQLException e) 
        {  
        	e.printStackTrace();
        }
        
        MessageDigest md;
		try 
		{
			md = MessageDigest.getInstance("MD5");
		} 
		catch (NoSuchAlgorithmException e) 
		{
			md = null;
		} 
		
		
		md.update(str.getBytes());
	    byte[] digest = md.digest();
	    String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        
        
	    return pw.equals(myHash);  	
    }
}

