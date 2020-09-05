package com.ome_r.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.ome_r.Crate;
import com.ome_r.Crates;

public class DamageEvent implements Listener{

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
		if(!(e.getEntity() instanceof ArmorStand))
			return;
		
		for(Crate c : Crates.crates)
			if(e.getEntity().equals(c.getArmorStand())){
				e.setCancelled(true);
				break;
			}
		
	}
	
}
