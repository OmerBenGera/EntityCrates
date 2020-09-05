package com.ome_r;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Crate implements Comparable<Crate>{

	private ArmorStand armorStand;
	private Location location;
	private Inventory inventory;
	private long time;
	private int id;
	
	public Crate(Entity e, List<ItemStack> drops){
		this.id = generateID();
		Crates.newCrate(this, e, drops);
	}
	
	public Crate(Location loc, Inventory inv, long time){
		this.id = generateID();
		Crates.newCrate(this, loc, inv, time);
	}
	
	public void delete(){
		armorStand.remove();
		Crates.crates.remove(this);
		EntityCrates.getDataManager().delCrate(this);
	}
	
	public void spawn(){
		ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		
		armorStand.setGravity(false);
		armorStand.setHelmet(new ItemStack(Material.CHEST));
		armorStand.setVisible(false);
		
		if(Crates.timerName)
			armorStand.setCustomNameVisible(true);
		
		this.armorStand = armorStand;
	}
	
	public void setArmorStand(ArmorStand armorStand){
		this.armorStand = armorStand;
	}
	
	public ArmorStand getArmorStand(){
		return this.armorStand;
	}
	
	public void setLocation(Location location){
		this.location = location;
	}
	
	public Location getLocation(){
		return this.location;
	}
	
	public void setInventory(Inventory inventory){
		this.inventory = inventory;
	}
	
	public Inventory getInventory(){
		return this.inventory;
	}
	
	public void setTime(long time){
		this.time = time;
	}
	
	public long getTime(){
		return this.time;
	}
	
	public int getUniqueID(){
		return this.id;
	}
	
	private int generateID(){
		int id = 1;
		while(Crates.exists(id))
			id++;
		return id;
	}

	@Override
	public int compareTo(Crate o) {
		return String.valueOf(this.getUniqueID()).compareTo(String.valueOf(o.getUniqueID()));
	}
	
}
