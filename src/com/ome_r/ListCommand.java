package com.ome_r;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class ListCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("§cOnly players have access to use this command.");
			return false;
		}
		Player p = (Player) sender;
		
		if(!p.hasPermission("entitycrates.list")){
			p.sendMessage("§fUnknown command. Type \"/help\" for help.");
			return false;
		}
		
		if(Crates.crates.isEmpty()){
			p.sendMessage(" \n§a§lEntityCrates §cCrates list is empty.\n ");
		}
		
		else{
			int page = 1, size = Crates.crates.size(), maxPage = (size / 10);
			if(args.length >= 1)
				if(NumberUtils.isNumber(args[0]))
					page = Integer.valueOf(args[0]);
			
			if(size % 10 != 0)
				maxPage++;
			
			if(((page - 1) * 10) >= size){
				p.sendMessage(" \n§a§lEntityCrates §cThis page is empty.\n ");
			}
			
			else{
				p.sendMessage("\n");
				p.sendMessage("§a§lEntityCrates §7Crates List: [Page " + page + " / " + maxPage + "]");
				for(Crate c : getCrates(page)){
					double x = c.getLocation().getX(), y = c.getLocation().getY() + 2, z = c.getLocation().getZ();
					String loc = c.getLocation().getWorld().getName() + ", " + ((int) x) + ", " + ((int) y) + ", " + ((int) z);
					TextComponent text = new TextComponent("§a§l" + c.getUniqueID() + "§8§l - §7" + loc + " [Click to teleport]");
					text.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tp " + x + " " + y + " " + z));
					p.spigot().sendMessage(text);
				}
				p.sendMessage("\n");
			}
			
		}
		
		return false;
	}

	private List<Crate> getCrates(int page){
		List<Crate> list = new ArrayList<>();
		for(int i = ((page - 1) * 10); i < (page * 10); i++){
			if(Crates.crates.size() <= i) break;
			list.add(Crates.crates.get(i));
		}
		Collections.sort(list);
		return list;
	}
	
}
