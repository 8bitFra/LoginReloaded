package fra.plugin.login;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;

public class Listeners implements Listener
{
public static Main main;
    
    public Listeners(final Main main) {
        Listeners.main = main;
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final String IPsaved = Listeners.main.getConfig().getString("Messages.IPsaved").replace("&", "§");
        final String Login = Listeners.main.getConfig().getString("Messages.Login").replace("&", "§");
        final String Register = Listeners.main.getConfig().getString("Messages.Register").replace("&", "§");
        if (Handler.isRegistered(p.getName())) {
        	Handler.Logout(p);
            if (Listeners.main.allowIPsaving) 
            {
            	String address = p.getAddress().toString().replace("/", "");
                final int Port = p.getAddress().getPort();
                address = address.replace(":" + Port, "");
                if (Handler.isIPSave(p, address)) 
                {
                	p.sendMessage(IPsaved);
                    Handler.SucessfullLogin(p);
                    Handler.Login(p);
                    Handler.removeEffects(p);
                }
                else 
                {
                    Listeners.main.addSaveList(p);
                    Handler.addEffects(p);
                    p.sendMessage(" ");
                    p.sendMessage(Login);
                    p.sendMessage(" ");
                }
            }
            else 
            {
                Handler.addEffects(p);
                Listeners.main.addSaveList(p);
                p.sendMessage(" ");
                p.sendMessage(Login);
                p.sendMessage(" ");
            }
        }
        else {
            Handler.addEffects(p);
            Listeners.main.addSaveList(p);
            p.sendMessage(" ");
            p.sendMessage(Register);
            p.sendMessage(" ");
        }
    }
    
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (Listeners.main.isSave(p)) {
            e.setCancelled(true);
            if (Handler.isRegistered(p.getName())) {
                final String Login = Listeners.main.getConfig().getString("Messages.Login").replace("&", "§");
                p.sendMessage(" ");
                p.sendMessage(Login);
                p.sendMessage(" ");
            }
            else {
                final String Register = Listeners.main.getConfig().getString("Messages.Register").replace("&", "§");
                p.sendMessage(" ");
                p.sendMessage(Register);
                p.sendMessage(" ");
            }
        }
    }
    
    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent e) {
    	try
    	{
    		final Player p = (Player)e.getEntity();
    		if (e.getEntity() instanceof Player && Listeners.main.isSave(p)) {
    			e.setCancelled(true);
    		}
    	}
    	catch(Exception ex) {}
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        Handler.Logout(p);
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent e) {
        final Player p = e.getPlayer();
        if (Listeners.main.isSave(p)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent e) {
        final Player p = e.getPlayer();
        if (Listeners.main.isSave(p)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onInventorySaver(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        if (Listeners.main.isSave(p)) {
            e.setCancelled(true);
            e.getView().close();
        }
    }
    
    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        final Player p = (Player)e.getWhoClicked();
        if (Listeners.main.isSave(p)) {
            e.setCancelled(true);
            e.getView().close();
        }
    }
    
    @EventHandler
    public void onInventoryDrop(final PlayerDropItemEvent e) {
        final Player p = e.getPlayer();
        if (Listeners.main.isSave(p)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPickup(final EntityPickupItemEvent e) {
    	try
    	{
    		final Player p = (Player) e.getEntity();
            if (Listeners.main.isSave(p)) {
                e.setCancelled(true);
            }
    	}
    	catch (Exception ex){}
        
    }
    
    @EventHandler
    public void onBlockCmds(final PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();
        if (Listeners.main.isSave(p)) {
            if (e.getMessage().toLowerCase().startsWith("/login") || e.getMessage().toLowerCase().startsWith("/register")) {
                e.setCancelled(false);
            }
            else {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (Listeners.main.isSave(p)) {
        	final org.bukkit.Location to = e.getFrom();
        	to.setPitch(e.getTo().getPitch());
            to.setYaw(e.getTo().getYaw());
            e.setTo(to);
        }
    }
    
    /*@EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        String[] args = event.getMessage().split(" ");
        if(args[0].equalsIgnoreCase("/command"))
        {
            
        	//event.setMessage(event.getMessage());
        	event.setCancelled(true);
        }
   }*/
}
