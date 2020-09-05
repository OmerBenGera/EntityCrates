package com.ome_r.data;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ome_r.Crate;

public class DataFile {
	
	public void saveCrate(Crate c){
		try {
			File f = new File("plugins/EntityCrates/crates/" + c.getUniqueID() + ".yml");
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
			
			cfg.set("location", 
					c.getLocation().getWorld().getName() + ";" + 
					c.getLocation().getX()+ ";" + 
					c.getLocation().getY()+ ";" +
					c.getLocation().getZ());
			
			cfg.set("time", c.getTime());
			
			cfg.set("inventory", invToString(c.getInventory()));
			
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void delCrate(Crate c){
		new File("plugins/EntityCrates/crates/" + c.getUniqueID() + ".yml").delete();
	}
	
	public void readData(){
		if(new File("plugins/EntityCrates/crates/").exists())
			for(File f : new File("plugins/EntityCrates/crates/").listFiles()){
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
				if((System.currentTimeMillis() / 1000L) < cfg.getLong("time")){
					String[] sLoc = cfg.getString("location").split(";");
					Location loc = new Location(
							Bukkit.getWorld(sLoc[0]), 
							Double.parseDouble(sLoc[1]), 
							Double.parseDouble(sLoc[2]), 
							Double.parseDouble(sLoc[3]));
					Inventory inv = invFromString(cfg.getString("inventory"));
					new Crate(loc, inv, cfg.getLong("time"));
				}else f.delete();
			}
		
	}
	
    @SuppressWarnings("deprecation")
	private String invToString (Inventory invInventory){
        String serialization = invInventory.getName().replaceAll("&", "~").replaceAll("§", "&") + ";" + invInventory.getSize() + ";";
        for (int i = 0; i < invInventory.getSize(); i++){
            ItemStack is = invInventory.getItem(i);
            if (is != null){
                String serializedItemStack = new String();
               
                String isType = String.valueOf(is.getType().getId());
                serializedItemStack += "t@" + isType;
               
                if (is.getDurability() != 0){
                    String isDurability = String.valueOf(is.getDurability());
                    serializedItemStack += ":d@" + isDurability;
                }
               
                if (is.getAmount() != 1){
                    String isAmount = String.valueOf(is.getAmount());
                    serializedItemStack += ":a@" + isAmount;
                }
               
                Map<Enchantment,Integer> isEnch = is.getEnchantments();
                if (isEnch.size() > 0)
                    for (Entry<Enchantment,Integer> ench : isEnch.entrySet())
                        serializedItemStack += ":e@" + ench.getKey().getId() + "@" + ench.getValue();
                
                if(is.getItemMeta().hasDisplayName())
                    serializedItemStack += ":n@" + is.getItemMeta().getDisplayName().replaceAll("&", "~").replaceAll("§", "&");
                
                if(is.getItemMeta().hasLore()){
                    String isLore = "";
                    for(String s : is.getItemMeta().getLore())
                    	isLore += s + "::";
                    serializedItemStack += ":l@" + isLore.replaceAll("&", "~").replaceAll("§", "&");
                }
               
                serialization += i + "#" + serializedItemStack + ";";
            }
        }
        return serialization;
    }
    
    
    @SuppressWarnings("deprecation")
	private Inventory invFromString (String invString){
        String[] serializedBlocks = invString.split(";");
        String invSize = serializedBlocks[1], invTitle = ChatColor.translateAlternateColorCodes('&', serializedBlocks[0]).replaceAll("~", "&");
        Inventory deserializedInventory = Bukkit.getServer().createInventory(null, Integer.valueOf(invSize), invTitle);
       
        for (int i = 2; i < serializedBlocks.length; i++){
            String[] serializedBlock = serializedBlocks[i].split("#");
            int stackPosition = Integer.valueOf(serializedBlock[0]);
           
            if (stackPosition >= deserializedInventory.getSize())
                continue;
           
            ItemStack is = null;
            ItemMeta meta = null;
            Boolean createdItemStack = false;
           
            String[] serializedItemStack = serializedBlock[1].split(":");
            for (String itemInfo : serializedItemStack) {
                String[] itemAttribute = itemInfo.split("@");
                if (itemAttribute[0].equals("t")){
                    is = new ItemStack(Material.getMaterial(Integer.valueOf(itemAttribute[1])));
                    meta = is.getItemMeta();
                    createdItemStack = true;
                }
                
                else if (itemAttribute[0].equals("d") && createdItemStack)
                    is.setDurability(Short.valueOf(itemAttribute[1]));
                
                else if (itemAttribute[0].equals("a") && createdItemStack)
                    is.setAmount(Integer.valueOf(itemAttribute[1]));
                
                else if (itemAttribute[0].equals("e") && createdItemStack)
                    is.addEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
                
                else if(itemAttribute[0].equals("n") && createdItemStack)
                	meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemAttribute[1]).replaceAll("~", "&"));
                
                else if(itemAttribute[0].equals("l") && createdItemStack)
                	meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', itemAttribute[1]).replaceAll("~", "&").split("::")));
                
                is.setItemMeta(meta);
                
            }
            deserializedInventory.setItem(stackPosition, is);
        }
       
        return deserializedInventory;
    }
	
}
