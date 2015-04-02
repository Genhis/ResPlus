package sk.genhis.resplus.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import sk.genhis.glib.Message;
import sk.genhis.resplus.ResPlus;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

public final class BukkitListener implements Listener {
	@EventHandler()
	public void onPlayerUseCommand(PlayerCommandPreprocessEvent e) {
		if(e.isCancelled())
			return;
		
		final Player p = e.getPlayer();
		final ClaimedResidence res = Residence.getResidenceManager().getByLoc(p.getLocation());
		if(res != null) {
			boolean cancel = false;
			
			ConfigurationSection root = ResPlus.getPlugin().getOwnConfig().getConfigurationSection("flag.commands");
			for(String key : root.getKeys(false)) {
				if(cancel)
					break;

				ConfigurationSection cs = root.getConfigurationSection(key);
				if(!res.getPermissions().playerHas(p.getName(), key, cs.getBoolean("default")) && !p.hasPermission("residence.admin")) {
					if(cs.getString("type", "whitelist").equalsIgnoreCase("whitelist")) { //whitelist
						cancel = true;
						for(String s : cs.getStringList("list"))
							if(e.getMessage().startsWith("/" + s))
								cancel = false;
					}
					else { //blacklist
						for(String s : cs.getStringList("list"))
							if(e.getMessage().startsWith("/" + s))
								cancel = true;
					}
				}
			}
			
			/*if(!res.getPermissions().playerHas(p.getName(), "command", true) && !p.hasPermission("residence.admin")) {
				cancel = true;
				for(String s : ResPlus.getPlugin().getOwnConfig().getStringList("flag.command.whitelist"))
					if(e.getMessage().startsWith("/" + s))
						cancel = false;
				
			}
			
			if(!res.getPermissions().playerHas(p.getName(), "setwarp", false) && !p.hasPermission("residence.admin")) {
				for(String s : ResPlus.getPlugin().getOwnConfig().getStringList("flag.setwarp.blacklist"))
					if(e.getMessage().startsWith("/" + s))
						cancel = true;
			}
			
			if(!res.getPermissions().playerHas(p.getName(), "npc", false) && !p.hasPermission("residence.admin")) {
				for(String s : ResPlus.getPlugin().getOwnConfig().getStringList("flag.npc.blacklist"))
					if(e.getMessage().startsWith("/" + s))
						cancel = true;
			}*/
			
			if(cancel) {
				Bukkit.getConsoleSender().sendMessage(Message.NO_PERMISSION.toString());
				p.sendMessage(Message.NO_PERMISSION.toString());
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler()
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if(!(e.getEntity() instanceof Player))
			return;
		final Player p = (Player)e.getEntity();
		final ClaimedResidence res = Residence.getResidenceManager().getByLoc(p.getLocation());
		
		if(res == null)
			return;
		
		if(e.getDamager() instanceof Snowball) {
			if(!res.getPermissions().playerHas(p.getName(), "snowball", true)) {
				p.sendMessage(Message.NO_PERMISSION.toString());
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getItem() == null || (e.getItem().getType() != Material.MINECART && e.getItem().getType() != Material.BOAT))
			return;
		
		final Player p = e.getPlayer();
		final ClaimedResidence res = Residence.getResidenceManager().getByLoc(p.getLocation());
		if(res == null)
			return;
		
		if(!res.getPermissions().playerHas(p.getName(), "vehiclecreate", true)) {
			p.sendMessage(Message.NO_PERMISSION.toString());
			e.setCancelled(true);
		}
	}
}