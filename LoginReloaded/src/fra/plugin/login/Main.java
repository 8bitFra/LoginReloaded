package fra.plugin.login;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener
{	
	{((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new PassFilter());}
	Logger log  = Logger.getLogger("Minecraft");
	
	
	public ArrayList<Player> player;
    public Boolean allowIPsaving;
    
    public Main() {
        this.player = new ArrayList<Player>();
        this.allowIPsaving = this.getConfig().getBoolean("AllowIPSaver");
    }
    
    public void onEnable() {
        this.loadConfig();
        
        File tempFile = new File("plugins/LoginReloaded/database.db");
		boolean exists = tempFile.exists();
		
		if(!exists)
		{
			CreateDatabase.inizializeDatabase();
		}
        
        this.getServer().getPluginManager().registerEvents((Listener)new Listeners(this), (Plugin)this);
        for (final Player all : Bukkit.getOnlinePlayers()) {
            Handler.addEffects(all);
            this.addSaveList(all);
            Handler.Logout(all);
            final String Login = this.getConfig().getString("Messages.Login").replace("&", "§");
            final String Register = this.getConfig().getString("Messages.Register").replace("&", "§");
            if (Handler.isRegistered(all.getName())) {
                all.sendMessage(Login);
            }
            else {
                all.sendMessage(Register);
            }
        }
        Handler.RefreshIPList();
        
        log.log(Level.INFO, String.format("[%s] Successfully enabled version %s!", getDescription().getName(), getDescription().getVersion()));
    }
    
    public void loadConfig() 
    {
        final FileConfiguration cfg = this.getConfig();
        cfg.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
    	
    	Handler.closeSqlConn();
        
        log.log(Level.INFO, String.format("[%s] Successfully disabled version %s!", getDescription().getName(), getDescription().getVersion()));
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        final String LoginSuccess = this.getConfig().getString("Messages.LoginSucces").replace("&", "§");
        final String Login = this.getConfig().getString("Messages.Login").replace("&", "§");
        final String Register = this.getConfig().getString("Messages.Register").replace("&", "§");
        final String Registered = this.getConfig().getString("Messages.AlreadyRegistered").replace("&", "§");
        final String AlreadyLoggedIn = this.getConfig().getString("Messages.AlreadyLoggedIn").replace("&", "§");
        final String RegistrationSuccess = this.getConfig().getString("Messages.RegistrationSuccess").replace("&", "§");
        final String ChoosenPW = this.getConfig().getString("Messages.ChoosenPW").replace("&", "§");
        final String ChangeUsage = this.getConfig().getString("Messages.ChangePWUsage").replace("&", "§");
        final String ChangeSuccess = this.getConfig().getString("Messages.ChangeSuccess").replace("&", "§");
        final String ChangeNotMatch = this.getConfig().getString("Messages.ChangeNotMatch").replace("&", "§");
        final String PWnotMatch = this.getConfig().getString("Messages.PwNotMatch").replace("&", "§");
        final String HaveToLogin = this.getConfig().getString("Messages.HaveToLogin").replace("&", "§");
        final String WrongPW = this.getConfig().getString("Messages.WrongLogin").replace("&", "§");
        final String NotOnline = this.getConfig().getString("Messages.NotOnline").replace("&", "§");
        final String NotPassword = this.getConfig().getString("Messages.NotPassword").replace("&", "§");
        final String MinSix = this.getConfig().getString("Messages.MinSix").replace("&", "§");
        final String Check1 = this.getConfig().getString("Check.Line1").replace("&", "§");
        final String Check2 = this.getConfig().getString("Check.Line2").replace("&", "§");
        final String Check3 = this.getConfig().getString("Check.Line3").replace("&", "§");
        final String Check4 = this.getConfig().getString("Check.Line4").replace("&", "§");
        final String Check5 = this.getConfig().getString("Check.Line5").replace("&", "§");
        final String Check6 = this.getConfig().getString("Check.Line6").replace("&", "§");
        final String Usage = this.getConfig().getString("Check.Usage").replace("&", "§");
        final Player p = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("register")) 
        {
            if (Handler.isRegistered(p.getName())) {
                p.sendMessage(Registered);
            }
            else {
                if (args.length == 0) {
                    p.sendMessage(Register);
                }
                if (args.length == 1) {
                    p.sendMessage(Register);
                }
                if (args.length == 2) {
                    if (args[0].equals(args[1])) {
                    	
                    	int count = 0;    
                        String password = args[1];
                        //Counts each character except space    
                        for(int i = 0; i < password.length(); i++) 
                        {    
                            if(password.charAt(i) != ' ')    
                                count++;    
                        }
                        
                        if(count < 6)
                        {
                        	p.sendMessage(MinSix);
                        }
                        else if (password.compareTo("password") == 0)
                        {
                        	p.sendMessage(NotPassword);
                        }
                        else
                        {
                        	Handler.RegisterPlayer(p, args[0]);
                            Handler.removeEffects(p);
                            p.sendMessage(RegistrationSuccess);
                            p.sendMessage(String.valueOf(ChoosenPW) + args[0]);
                            this.removeSaveList(p);
                            Handler.Login(p);
                            if (this.allowIPsaving) {
                                String address = p.getAddress().toString().replace("/", "");
                                final int Port = p.getAddress().getPort();
                                address = address.replace(":" + Port, "");
                                Handler.RegisterNewIP(p, address);
                            }
                            else {
                            	System.out.println("[LOGIN] IP was not saved.");
                                System.out.println("[LOGIN] Activate IP storage in the config.");
                            }
                        }
                    	                     
                    }
                    else {
                        p.sendMessage(PWnotMatch);
                        Handler.WrongLogin(p);
                    }
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("login")) 
        {
            if (Handler.isRegistered(p.getName())) {
                if (Handler.isLoggedIn(p.getName())) {
                    p.sendMessage(AlreadyLoggedIn);
                }
                else {
                    if (args.length == 0) {
                        p.sendMessage(Login);
                    }
                    if (args.length == 1) {
                        final String clan = args[0];
                        if (Handler.PasswordMatch(p, clan)) {
                            p.sendMessage(LoginSuccess);
                            Handler.removeEffects(p);
                            this.removeSaveList(p);
                            Handler.Login(p);
                            if (this.allowIPsaving) 
                            {
                                String address2 = p.getAddress().toString().replace("/", "");
                                final int Port2 = p.getAddress().getPort();
                                address2 = address2.replace(":" + Port2, "");
                                Handler.RegisterNewIP(p, address2);
                            }
                            else 
                            {
                                System.out.println("[LOGIN] IP was not saved.");
                                System.out.println("[LOGIN] Activate IP storage in the config.");
                            }
                        }
                        else {
                            p.sendMessage(WrongPW);
                            Handler.WrongLogin(p);
                        }
                    }
                }
            }
            else {
                p.sendMessage(Register);
            }
        }
        else if (cmd.getName().equalsIgnoreCase("logout"))
        {
        	if (Handler.isRegistered(p.getName())) 
        	{
                if (Handler.isLoggedIn(p.getName())) 
                {
                    Handler.LogoutCommand(p);
                }
        	}
        }
        else if (cmd.getName().equalsIgnoreCase("check")) 
        {
            if (p.hasPermission("loginreloaded.check")) {
                if (args.length == 0) {
                    p.sendMessage(Usage);
                }
                if (args.length == 1) 
                {
                    p.sendMessage(String.valueOf(Check1) + args[0]);
                    if (Handler.isRegistered(args[0])) {
                        p.sendMessage(Check2);                                
     
                        p.sendMessage(String.valueOf(Check4) + Handler.isOnline(args[0]));
                        p.sendMessage(String.valueOf(Check5) + Handler.getIpAddress(args[0]));
                        p.sendMessage(String.valueOf(Check6) + Handler.getMD5Password(args[0]));
                    }
                    else {
                        p.sendMessage(Check3);
                    }
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("resetpw"))
        {
        	if (p.hasPermission("loginreloaded.resetpw")) 
        	{
        		if (args.length != 0)
           	 	{
        			try
        			{
        				final Player target = getServer().getPlayer(args[0]);
               		 	if (Handler.isRegistered(target.getName())) 
               		 	{
               		 		Handler.ResetPassword(target);
               		 	}
        			}
           		 	catch(Exception e)
        			{
           		 		p.sendMessage(NotOnline);
        			}
           	 	}
        	}
        	 
        }
        else if (cmd.getName().equalsIgnoreCase("changepw")) 
        {
            if (Handler.isLoggedIn(p.getName())) {
                if (args.length == 0) {
                    p.sendMessage(ChangeUsage);
                }
                if (args.length == 1) {
                    p.sendMessage(ChangeUsage);
                }
                if (args.length == 2) {
                    if (args[0].equals(args[1])) {
                        Handler.ChangePassword(p, args[0]);
                        p.sendMessage(String.valueOf(ChangeSuccess) + args[0]);
                    }
                    else {
                        p.sendMessage(ChangeNotMatch);
                    }
                }
            }
            else {
                p.sendMessage(HaveToLogin);
            }
        }
        return false;
    }
    
    public void addSaveList(final Player p) {
        this.player.add(p);
    }
    
    public boolean isSave(final Player p) {
        return this.player.contains(p);
    }
    
    public void removeSaveList(final Player p) {
        this.player.remove(p);
    }
	
	
}
