package com.ome_r.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.ome_r.Crate;
import com.ome_r.Crates;

public class DeathEvent implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		if(!Crates.entitiesList.contains(e.getEntityType()))
			return;
		
		new Crate(e.getEntity(), e.getDrops());
		e.getDrops().clear();
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e){
		if(!Crates.entitiesList.contains(e.getEntityType()))
			return;
		
		new Crate(e.getEntity(), e.getDrops());
		e.getDrops().clear();
	}
	
}
