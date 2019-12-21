package sk.genhis.resplus.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import sk.genhis.glib.GLib;
import sk.genhis.glib.scheduler.GTask;
import sk.genhis.resplus.ResPlus;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

import fr.xephi.authme.events.AuthMeTeleportEvent;

public final class AuthMeListener implements Listener {
	@EventHandler(priority=EventPriority.MONITOR)
	public void teleport(AuthMeTeleportEvent e) {
		final Player p = e.getPlayer();
		final ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(e.getTo());
		if(res != null && !p.hasPermission("residence.admin") && res.getPermissions().playerHas(p.getName(), "antilogin", false)) {
			GLib.getScheduler().scheduleSyncDelayedTask(ResPlus.getPlugin(), new GTask() {
				public void run() {
					p.sendMessage(ChatColor.DARK_RED + "Byl jsi zabit za pripojeni v zakazane oblasti.");
					p.setHealth(0.0);
				}
				
				public String getName() {
					return "AntiLoginKiller";
				}
			}, 5L);
		}
	}
}