package sk.genhis.resplus.listener;

import java.util.Calendar;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.griefcraft.lwc.LWC;

public class LWCListener implements Listener {

	private Calendar c;
	
	public LWCListener(Calendar c) {
		super();
		this.c = c;
	}
	
	@EventHandler
	public void onChestOpen(PlayerInteractEvent e) {
		if(e.getClickedBlock() != null && e.getClickedBlock().getType().equals(Material.CHEST)) {
			final ClaimedResidence res = Residence.getResidenceManager().getByLoc(e.getClickedBlock().getLocation());
			if(res != null && res.getPermissions().has("christmas", false)) {
				if(LWC.getInstance().canAccessProtection(e.getPlayer(), e.getClickedBlock())) {
					Calendar d = Calendar.getInstance();
					if(d.before(c)) {
						e.getPlayer().sendMessage(ChatColor.RED+"Tato bedna pùjde otevøít až "+c.get(Calendar.DAY_OF_MONTH)+". "+(c.get(Calendar.MONTH)+1)+". "+c.get(Calendar.YEAR)+" v "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE));
						e.setCancelled(true);
						return;
					}
				}
			}
		}
			
	}
}
