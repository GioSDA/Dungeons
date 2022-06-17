package io.github.giosda.dungeons.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import io.github.giosda.dungeons.Dungeons;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Commands implements CommandExecutor {


	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(Color.RED + "What do you want to do?");
			return true;
		}

		if (args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(Color.YELLOW + "miny <num>:" + Color.AQUA + "set the minimum y value to <num>");
			sender.sendMessage(Color.YELLOW + "maxy <num>:" + Color.AQUA + "set the maximum y value to <num>");
			sender.sendMessage(Color.YELLOW + "chance <num>:" + Color.AQUA + "set the spawn percentage to <num>");
			sender.sendMessage(Color.YELLOW + "create <name>:" + Color.AQUA + "create a new schematic called <name>");
			sender.sendMessage(Color.YELLOW + "delete <name>:" + Color.AQUA + "delete the schematic called <name>");
			return true;
		}

		if (args[0].equalsIgnoreCase("miny")) {
			if (args.length == 1) {
				sender.sendMessage(Color.RED + "What would you like to set the minimum y value to?");
				return true;
			}

			try {
				Dungeons.config.set("min-y", Integer.valueOf(args[2]));
			} catch (NumberFormatException e) {
				sender.sendMessage(Color.RED + "That is not a number!");
			}
		}

		if (args[0].equalsIgnoreCase("maxy")) {
			if (args.length == 1) {
				sender.sendMessage(Color.RED + "What would you like to set the maximum y value to?");
				return true;
			}

			try {
				Dungeons.config.set("max-y", Integer.valueOf(args[2]));
			} catch (NumberFormatException e) {
				sender.sendMessage(Color.RED + "That is not a number!");
			}
		}

		if (args[0].equalsIgnoreCase("chance")) {
			if (args.length == 1) {
				sender.sendMessage(Color.RED + "What would you like to set the percentage chance to?");
				return true;
			}

			try {
				Dungeons.config.set("percentage-chance", Double.valueOf(args[2]));
			} catch (NumberFormatException e) {
				sender.sendMessage(Color.RED + "That is not a number!");
			}
		}

		if (args[0].equalsIgnoreCase("create")) {
			if (args.length == 1) {
				sender.sendMessage(Color.RED + "What would you like to name the schematic?");
				return true;
			}

			try {
				Selection s = Dungeons.worldEditPlugin.getSelection((Player) sender);

				if (s == null) {
					sender.sendMessage(Color.RED + "Please make a selection!");
					return true;
				}

				File file = new File("/dungeons/" + args[1] + ".schematic")/* figure out where to save the clipboard */;

				try (ClipboardWriter writer = ClipboardFormat.SCHEMATIC.getWriter(new FileOutputStream("/" + file))) {
					writer.write(new BlockArrayClipboard(s.getRegionSelector().getRegion()), s.getRegionSelector().getRegion().getWorld().getWorldData());
				} catch (IOException | IncompleteRegionException e) {
					sender.sendMessage(Color.RED + "An error has occurred! Code: 101");
					return true;
				}
			} catch (NumberFormatException e) {
				sender.sendMessage(Color.RED + "An error has occurred! Code: 102");
				return true;
			}
		}

		return true;
	}
}
