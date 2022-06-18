package io.github.giosda.dungeons.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import io.github.giosda.dungeons.Dungeons;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Commands implements CommandExecutor {


	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("§9---------------------------------------");
			sender.sendMessage("§e" + "miny <num>: " + "§b" + "set the minimum y value to <num>");
			sender.sendMessage("§e" + "maxy <num>: " + "§b" + "set the maximum y value to <num>");
			sender.sendMessage("§e" + "chance <num>: " + "§b" + "set the spawn percentage to <num>");
			sender.sendMessage("§e" + "create <name>: " + "§b" + "create a new dungeon called <name>");
			sender.sendMessage("§e" + "delete <name>: " + "§b" + "delete the dungeon called <name>");
			sender.sendMessage("§9---------------------------------------");
			return true;
		}

		if (args[0].equalsIgnoreCase("help")) {
			sender.sendMessage("§9---------------------------------------");
			sender.sendMessage("§e" + "miny <num>: " + "§b" + "set the minimum y value to <num>");
			sender.sendMessage("§e" + "maxy <num>:" + "§b" + "set the maximum y value to <num>");
			sender.sendMessage("§e" + "chance <num>: " + "§b" + "set the spawn percentage to <num>");
			sender.sendMessage("§e" + "create <name>: " + "§b" + "create a new dungeon called <name>");
			sender.sendMessage("§e" + "delete <name>: " + "§b" + "delete the dungeon called <name>");
			sender.sendMessage("§9---------------------------------------");
			return true;
		}

		if (args[0].equalsIgnoreCase("miny")) {
			if (args.length == 1) {
				sender.sendMessage("§c" + "What would you like to set the minimum y value to?");
				return true;
			}

			try {
				Dungeons.config.set("min-y", Integer.valueOf(args[1]));
				sender.sendMessage("§a" + "Mininum y value set to §e" + args[1] + "§a Reload for these changes to take effect.");
				return true;
			} catch (NumberFormatException e) {
				sender.sendMessage("§c" + "That is not a number!");
				return true;
			}
		}

		if (args[0].equalsIgnoreCase("maxy")) {
			if (args.length == 1) {
				sender.sendMessage("§c" + "What would you like to set the maximum y value to?");
				return true;
			}

			try {
				Dungeons.config.set("max-y", Integer.valueOf(args[1]));
				sender.sendMessage("§a" + "Maximum y value set to §e" + args[1] + "§a Reload for these changes to take effect.");
				return true;
			} catch (NumberFormatException e) {
				sender.sendMessage("§c" + "That is not a number!");
				return true;
			}
		}

		if (args[0].equalsIgnoreCase("chance")) {
			if (args.length == 1) {
				sender.sendMessage("§c" + "What would you like to set the percentage chance to?");
				return true;
			}

			try {
				Dungeons.config.set("percentage-chance", Double.valueOf(args[1]));
				sender.sendMessage("§a" + "Spawn chance set to §e" + args[1] + "% §a Reload for these changes to take effect.");
				return true;
			} catch (NumberFormatException e) {
				sender.sendMessage("§c" + "That is not a number!");
				return true;
			}
		}

		if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("edit")) {
			Selection s = Dungeons.worldEditPlugin.getSelection((Player) sender);

			if (s == null) {
				sender.sendMessage("§c" + "Please make a selection!");
				return true;
			}

			if (args.length == 1) {
				sender.sendMessage("§c" + "What would you like to name the dungeon?");
				return true;
			}


			try {
				File file = new File(Dungeons.dungeons.getDataFolder().getAbsolutePath() + "/dungeons/" + args[1] + ".schematic");

				CuboidRegion region = new CuboidRegion(s.getNativeMinimumPoint(), s.getNativeMaximumPoint());
				BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

				ClipboardWriter writer = ClipboardFormat.SCHEMATIC.getWriter(new FileOutputStream("/" + file));
				writer.write(clipboard, Objects.requireNonNull(s.getRegionSelector().getRegion().getWorld()).getWorldData());
				writer.close();

				sender.sendMessage("§a" + "Dungeon saved with name §e" + args[1] + "§a Reload for these changes to take effect.");
				return true;
			} catch (IOException | IncompleteRegionException e) {
				e.printStackTrace();
				sender.sendMessage("§c" + "An error has occurred! Code: 101.");
				return true;
			}
		}

		if (args[0].equalsIgnoreCase("delete")) {
			if (args.length == 1) {
				sender.sendMessage("§c" + "What is the name of the dungeon you would like to delete?");
				return true;
			}

			File file = new File(Dungeons.dungeons.getDataFolder().getAbsolutePath() + "/dungeons/" + args[1] + ".schematic")/* figure out where to save the clipboard */;

			if (file.delete()) {
				sender.sendMessage("§a" + "Dungeon §e" + args[1] + "§a has been deleted." + "§a Reload for these changes to take effect.");
				return true;
			} else {
				sender.sendMessage("§c" + "An error has occured! Code: 103. Are you sure the dungeon exists?");
				return true;
			}
		}

		sender.sendMessage("§9---------------------------------------");
		sender.sendMessage("§e" + "miny <num>: " + "§b" + "set the minimum y value to <num>");
		sender.sendMessage("§e" + "maxy <num>: " + "§b" + "set the maximum y value to <num>");
		sender.sendMessage("§e" + "chance <num>: " + "§b" + "set the spawn percentage to <num>");
		sender.sendMessage("§e" + "create <name>: " + "§b" + "create a new dungeon called <name>");
		sender.sendMessage("§e" + "delete <name>: " + "§b" + "delete the dungeon called <name>");
		sender.sendMessage("§9---------------------------------------");

		return true;
	}
}
