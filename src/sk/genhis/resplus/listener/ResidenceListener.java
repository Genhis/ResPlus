package sk.genhis.resplus.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bekvon.bukkit.residence.event.ResidenceTPEvent;

public class ResidenceListener implements Listener {

	@EventHandler
	public void onResTP(ResidenceTPEvent e) {
		if(!e.isAdmin())
			if(!e.getResidence().getOwner().equals(e.getRequestingPlayer().getName())) {
				e.getRequestingPlayer().sendMessage(ChatColor.RED+"Nejsi majitelem teto residence!");
				e.setCancelled(true);
				
				return;
			}
				
		
	}
}
