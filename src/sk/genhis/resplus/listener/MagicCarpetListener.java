package sk.genhis.resplus.listener;

import net.digiex.magiccarpet.MagicCarpet;
import net.digiex.magiccarpet.Storage;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import sk.genhis.glib.Message;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.event.ResidenceChangedEvent;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

public final class MagicCarpetListener implements Listener {
	@EventHandler(priority=EventPriority.MONITOR)
	public void onResidenceEnter(ResidenceChangedEvent e) {
		if(e.getTo() != null && !e.getPlayer().hasPermission("residence.admin") && !e.getTo().getPermissions().playerHas(e.getPlayer().getName(), "magiccarpet", true) && Storage.getCarpet(e.getPlayer()).isVisible()) {
			e.getPlayer().sendMessage(Message.NO_PERMISSION.toString());
			Storage.getCarpet(e.getPlayer()).hide();
		}
	}
	
	@EventHandler()
	public void onPlayerUseCommand(PlayerCommandPreprocessEvent e) {
		if(e.isCancelled())
			return;
		
		final Player p = e.getPlayer();
		final ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(p.getLocation());
		if(res != null && !e.getPlayer().hasPermission("residence.admin") && !res.getPermissions().playerHas(p.getName(), "magiccarpet", true)) {
			if(e.getMessage().startsWith("/mc") || e.getMessage().startsWith("/magiccarpet")) {
				e.getPlayer().sendMessage(Message.NO_PERMISSION.toString());
				e.setCancelled(true);
			}
		}
	}
}