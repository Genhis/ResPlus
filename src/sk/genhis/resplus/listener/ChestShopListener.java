package sk.genhis.resplus.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.PreShopCreationEvent.CreationOutcome;
import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import com.Acrobot.ChestShop.Events.PreTransactionEvent.TransactionOutcome;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

public final class ChestShopListener implements Listener {
	@EventHandler(priority=EventPriority.HIGHEST)
	public void preTransaction(PreTransactionEvent e) {
		final Player p = e.getClient();
		final ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(p.getLocation());
		if(res != null) {
			boolean cancel = false;
			
			if(!res.getPermissions().playerHas(p.getName(), "shopuse", true) &&
					!p.hasPermission("residence.admin"))
				cancel = true;
			
			if(cancel)
				e.setCancelled(TransactionOutcome.CLIENT_DOES_NOT_HAVE_PERMISSION);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void preShopCreation(PreShopCreationEvent e) {
		final Player p = e.getPlayer();
		final ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(p.getLocation());
		if(res != null) {
			boolean cancel = false;
			
			if(!res.getPermissions().playerHas(p.getName(), "shopcreate", false) &&
					!p.hasPermission("residence.admin"))
				cancel = true;
			
			if(cancel)
				e.setOutcome(CreationOutcome.NO_PERMISSION_FOR_TERRAIN);
		}
	}
}