package com.ome_r.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.ome_r.Crate;
import com.ome_r.Crates;

public class CloseInventoryEvent implements Listener{

	@EventHandler
	public void onCloseInventory(InventoryCloseEvent e){
		for(Crate c : Crates.crates)
			if(c.getInventory().equals(e.getInventory()) && Crates.isEmpty(e.getInventory()) && 
					e.getInventory().getViewers().size() <= 1){
				c.delete();
				break;
			}
	}
	
}
