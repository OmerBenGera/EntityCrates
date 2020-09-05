package com.ome_r;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.ome_r.data.Config;
import com.ome_r.data.DataFile;
import com.ome_r.listeners.CloseInventoryEvent;
import com.ome_r.listeners.DamageEvent;
import com.ome_r.listeners.DeathEvent;
import com.ome_r.listeners.InteractEvent;

public class EntityCrates extends JavaPlugin {
	
	private static EntityCrates instance;
	private static DataFile dataManager;
	private static Config config;
	
	@Override
	public void onEnable() {
		instance = this;
		dataManager = new DataFile();
		config = new Config();
		Crates.moveCrates();
		
		loadYMLFiles();
		loadCommands();
		loadListeners(new DamageEvent(), new DeathEvent(), new InteractEvent(), new CloseInventoryEvent());
	}
	
	@Override
	public void onDisable() {
		Crates.crates.forEach(c -> {
			dataManager.saveCrate(c);
			c.getArmorStand().remove();
		});
	}
	
	private void loadListeners(Listener... listeners){
		PluginManager pm = getServer().getPluginManager();
		for(Listener s : listeners)
			pm.registerEvents(s, this);
	}
	
	private void loadCommands(){
		getCommand("crateslist").setExecutor(new ListCommand());
	}
	
	private void loadYMLFiles(){
		config.setStandard();
		config.readData();
		dataManager.readData();
	}
	
	public static EntityCrates getInstance(){
		return instance;
	}
	
	public static DataFile getDataManager(){
		return dataManager;
	}
	
}
