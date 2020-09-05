package com.ome_r.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import com.ome_r.Crate;
import com.ome_r.Crates;

public class InteractEvent implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractAtEntityEvent e){
		if(!(e.getRightClicked() instanceof ArmorStand))
			return;
		
		for(Crate c : Crates.crates)
			if(e.getRightClicked().equals(c.getArmorStand())){
				e.setCancelled(true);
				e.getPlayer().openInventory(c.getInventory());
				break;
			}
		
	}
	
}
