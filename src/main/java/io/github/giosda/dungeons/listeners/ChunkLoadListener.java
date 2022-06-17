package io.github.giosda.dungeons.listeners;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import io.github.giosda.dungeons.Dungeons;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ChunkLoadListener implements Listener {

	Random r = new Random();

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e) {
		if (!e.isNewChunk()) return;
		if (!e.getWorld().getName().endsWith("world")) return;

		double random = r.nextDouble();
		if (random < Dungeons.percentage / 100d) {
			Location l = new Location(e.getWorld(), e.getChunk().getX()*16 + r.nextInt(16), r.nextInt(Dungeons.max - Dungeons.min) + Dungeons.min, e.getChunk().getZ()*16 + r.nextInt(16));
			loadSchematic(l);
		}
	}

	private void loadSchematic(Location location) {

		File path = Dungeons.schematicPaths.get(r.nextInt(Dungeons.schematicPaths.size()-1));
		EditSession session = Dungeons.worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 10000);

		try {
			CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(path).load(path);
			clipboard.paste(session, new Vector(location.getX(), location.getY(), location.getZ()), false);

		} catch (MaxChangedBlocksException | DataException | IOException e) {
			e.printStackTrace();
		}
	}

}
