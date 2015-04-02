package sk.genhis.resplus;

import org.bukkit.Bukkit;

import com.bekvon.bukkit.residence.protection.FlagPermissions;

import sk.genhis.glib.LicenceChecker;
import sk.genhis.glib.Logger;
import sk.genhis.glib.configuration.Config;
import sk.genhis.glib.configuration.Configuration;
import sk.genhis.glib.plugin.GPlugin;
import sk.genhis.resplus.listener.AuthMeListener;
import sk.genhis.resplus.listener.ChestShopListener;
import sk.genhis.resplus.listener.BukkitListener;
import sk.genhis.resplus.listener.MagicCarpetListener;

public final class ResPlus extends GPlugin {
	private static GPlugin plugin;
	private static Logger logger;
	private static Configuration config;
	
	protected boolean enable() {
		ResPlus.plugin = this;
		ResPlus.logger = new Logger("[" + this.getDescription().getName() + "]");
		ResPlus.config = new Configuration(new Config(this).getConfig());
		
		ResPlus.logger.log("Kontrolujem licenciu");
		final LicenceChecker c = new LicenceChecker(this);
		if(!c.checkLicence()) {
			//c.unlicenced();
			//return false;
		}
		
		ResPlus.logger.log("Kontrolujem pluginy pre integraciu");
		boolean authme = Bukkit.getPluginManager().getPlugin("AuthMe") != null;
		boolean chestshop = Bukkit.getPluginManager().getPlugin("ChestShop") != null;
		boolean magiccarpet = Bukkit.getPluginManager().getPlugin("MagicCarpet") != null;

		if(authme)
			ResPlus.logger.log("Zapinam integraciu s pluginom AuthMe");
		if(chestshop)
			ResPlus.logger.log("Zapinam integraciu s pluginom ChestShop");
		if(magiccarpet)
			ResPlus.logger.log("Zapinam integraciu s pluginom MagicCarpet");
		
		ResPlus.logger.log("Registrujem flagy pre plugin Residence");
		FlagPermissions.addFlag("snowball");
		FlagPermissions.addFlag("vehiclecreate");
		if(authme)
			FlagPermissions.addFlag("antilogin");
		if(chestshop) {
			FlagPermissions.addFlag("shopuse");
			FlagPermissions.addFlag("shopcreate");
		}
		if(magiccarpet)
			FlagPermissions.addFlag("magiccarpet");
		
		//registrujem vlastné flagy - príkazy
		for(String key : ResPlus.getPlugin().getOwnConfig().getConfigurationSection("flag.commands").getKeys(false)) {
			Bukkit.broadcastMessage(key);
			FlagPermissions.addFlag(key);
		}
		
		ResPlus.logger.log("Registrujem listenery");
		Bukkit.getPluginManager().registerEvents(new BukkitListener(), this);
		if(authme)
			Bukkit.getPluginManager().registerEvents(new AuthMeListener(), this);
		if(chestshop)
			Bukkit.getPluginManager().registerEvents(new ChestShopListener(), this);
		if(magiccarpet)
			Bukkit.getPluginManager().registerEvents(new MagicCarpetListener(), this);
		return true;
	}
	
	public static GPlugin getPlugin() {
		return ResPlus.plugin;
	}
	
	public Logger getOwnLogger() {
		return ResPlus.logger;
	}
	
	public Configuration getOwnConfig() {
		return ResPlus.config;
	}
	
	public boolean debug() {
		return ResPlus.getDebug();
	}
	
	public static boolean getDebug() {
		return ResPlus.config.getBoolean("debug", true);
	}
	
	protected boolean enableMysql() {
		return false;
	}
}