package com.ome_r;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Crates {

	public static List<EntityType> entitiesList = new ArrayList<>();
	public static List<Crate> crates = new ArrayList<>();
	public static boolean customNames, codedNames, timerName;
	public static long interval;
	
	@SuppressWarnings("deprecation")
	public static void moveCrates(){
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(EntityCrates.getPlugin(EntityCrates.class), new Runnable() {
			
			@Override
			public void run() {
				if(!crates.isEmpty())
					for(Crate c : crates){
						
						//Checks if armorStand should die
						if((System.currentTimeMillis() / 1000L) >= c.getTime()){
							c.delete();
							break;
						}
						
						//Moves the crate
						else{
							if(c.getArmorStand().isDead()){
								c.delete();
								break;
							}
							
							c.getArmorStand().getLocation().getChunk().isLoaded();
							Location loc = c.getArmorStand().getLocation();
							loc.setYaw(loc.getYaw() + 5);
							c.getArmorStand().teleport(loc);
							
							if(timerName){
								int timeLeft = (int) (c.getTime() - (System.currentTimeMillis() / 1000L));
								String minutesLeft = (timeLeft / 60) + "", secondsLeft = (timeLeft % 60) + "";
								
								//Seconds fix
								if(Integer.valueOf(secondsLeft) < 10)
									secondsLeft = 0 + secondsLeft;
								
								c.getArmorStand().setCustomName("§c§l" + minutesLeft + ":" + secondsLeft);
							}
							
						}
						
					}
			}
		}, 0, 1);
	}

	public static void newCrate(Crate c, Entity e, List<ItemStack> drops){
		Inventory inv = getInventory(e, drops);
		Location loc = getLocation(e.getLocation());
		if(isEmpty(inv)) return;
		if(loc == null) return;
		
		ArmorStand armorStand = (ArmorStand) e.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		
		armorStand.setGravity(false);
		armorStand.setHelmet(new ItemStack(Material.CHEST));
		armorStand.setVisible(false);
		
		if(timerName)
			armorStand.setCustomNameVisible(true);
		
		c.setArmorStand(armorStand);
		c.setLocation(loc);
		c.setInventory(inv);
		c.setTime((System.currentTimeMillis() / 1000L) + interval);
		crates.add(c);
		EntityCrates.getDataManager().saveCrate(c);
	}

	public static void newCrate(Crate c, Location loc, Inventory inv, long time){
		if(isEmpty(inv)) return;
		
		ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		
		armorStand.setGravity(false);
		armorStand.setHelmet(new ItemStack(Material.CHEST));
		armorStand.setVisible(false);

		if(timerName)
			armorStand.setCustomNameVisible(true);
		
		c.setArmorStand(armorStand);
		c.setLocation(loc);
		c.setInventory(inv);
		c.setTime(time);
		crates.add(c);
	}
	
	public static boolean exists(int id){
		for(Crate c : crates)
			if(c.getUniqueID() == id) return true;
		
		return false;
	}
	
	public static boolean isEmpty(Inventory inv){
		for(ItemStack is : inv.getContents())
			if(is != null) return false;
		
		return true;
	}
	
	private static Inventory getInventory(Entity e, List<ItemStack> drops){
		String entity = getName(e);
		
		List<ItemStack> contents = new ArrayList<>();
		if(drops != null && !drops.isEmpty())
			contents = drops;
		
		int size = 0;
		while(contents.size() > size)
			size += 9;
		
		Inventory inv = Bukkit.createInventory(null, size, "Backpack (" + entity + "§r)");
		contents.forEach(is -> inv.addItem(is));
		
		return inv;
	}
	
	private static Location getLocation(Location loc){
		int y = loc.getBlockY();
		Block b = loc.getWorld().getBlockAt(loc.getBlockX(), y, loc.getBlockZ());
		
		while(b == null || b.getType() == Material.AIR || !b.getType().isSolid()){
			if(y < 0) return null;
			y--;
			b = loc.getWorld().getBlockAt(loc.getBlockX(), y, loc.getBlockZ());
		}
		
		return new Location(loc.getWorld(), loc.getX(), y - 0.3, loc.getZ());
	}
	
	private static String getName(Entity e){
		String name = e.getType().name().substring(0, 1).toUpperCase() + e.getType().name().substring(1).toLowerCase();
		
		if(e.getType().equals(EntityType.PLAYER))
			name = e.getName();
		
		else if(e.getType().equals(EntityType.SKELETON) && ((Skeleton) e).getSkeletonType().equals(SkeletonType.WITHER))
			name = "Wither Skeleton";
		
		else if(e.getType().equals(EntityType.HORSE))
			switch(((Horse) e).getVariant()){
				case DONKEY:
					name = "Donkey";
					break;
				case MULE:
					name = "Mule";
					break;
				case SKELETON_HORSE:
					name = "Skeleton Horse";
					break;
				case UNDEAD_HORSE:
					name = "Undead Horse";
					break;
				default:
					break;
			}
		
		if(customNames && e.getCustomName() != null){
			if(e.getType().equals(EntityType.SKELETON) && !e.getCustomName().equals("Skeleton"))
				name = e.getCustomName();
			
			else if(e.getType().equals(EntityType.HORSE) && !e.getCustomName().equals("Horse"))
				name = e.getCustomName();
		}

		
		if(!codedNames)
			name = ChatColor.stripColor(name);
		
		return name;
	}
	
}
