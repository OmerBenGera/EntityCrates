package com.ome_r.data;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import com.ome_r.Crates;
import com.ome_r.EntityCrates;

public class Config {

	private File file;
	private YamlConfiguration config;
	
	public Config(){
		file = new File("plugins/EntityCrates/config.yml");
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void setStandard(){
		if(!file.exists()){
			config.addDefault("save-interval", 600);
			
			config.addDefault("title-custom-names.enabled", true);
			config.addDefault("title-custom-names.color-codes", true);
			
			config.addDefault("armor-stand.timer-name", true);
			
			config.addDefault("entites-list", Arrays.asList("PLAYER"));
			
			EntityCrates.getInstance().saveResource("config.yml", false);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void readData(){
		Crates.customNames = config.getBoolean("title-custom-names.enabled");
		Crates.codedNames = config.getBoolean("title-custom-names.color-codes");
		Crates.timerName = config.getBoolean("armor-stand.timer-name");
		Crates.interval = config.getLong("save-interval");
		
		List<String> list = config.getStringList("entites-list");
		for(String s : list){
			if(s.equalsIgnoreCase("PLAYER")) Crates.entitiesList.add(EntityType.PLAYER);
			else if(EntityType.fromName(s.toUpperCase()) != null)
				Crates.entitiesList.add(EntityType.fromName(s.toUpperCase()));
			else System.err.println("[EntityCrates] The entity " + s + " is invalid.");
		}
	}
	
	public YamlConfiguration getConfig(){
		return config;
	}
	
	public File getFile(){
		return file;
	}
	
}
