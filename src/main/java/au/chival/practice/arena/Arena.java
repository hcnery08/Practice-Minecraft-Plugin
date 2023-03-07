package au.chival.practice.arena;

import au.chival.practice.util.FileConfig;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import static au.chival.practice.Main.*;
import static au.chival.practice.arena.ArenaManager.activeArenas;
import static au.chival.practice.util.Util.deleteTempWorld;
import static net.minecraft.server.v1_8_R3.MinecraftServer.LOGGER;

public class Arena {

    private final String arenaID;
    private final UUID uuid;

    private ConfigurationSection settings;
    private Clipboard clipboard;
    private File schematicFile;
    private List<Location> locationList;
    private org.bukkit.World world;

    protected Arena(String arenaID) {

        //add objects
        this.arenaID = arenaID;
        this.uuid = UUID.randomUUID();
        this.schematicFile = new File(JAVAPLUGIN.getDataFolder().getAbsoluteFile() + File.separator + "schematics" + File.separator + arenaID + ".schematic");
        this.settings = FileConfig.getConfig("data.yml").getConfigurationSection("arenas." + arenaID);

        //test if schematic file exists
        if (!schematicFile.exists()) {
            JAVAPLUGIN.getLogger().warning(arenaID + ".schematic does not exist in schematic folder");
            return;
        }

        //test if yml exists
        if (settings == null) {
            JAVAPLUGIN.getLogger().warning(arenaID + "'s YML does not exist");
            return;
        }

        //gets locations and tests if locations is corrupt
        try {
            locationList = (List<Location>) settings.getList("locations");
        } catch (Exception e) {
            JAVAPLUGIN.getLogger().warning(arenaID + "'s YML locations is corrupt");
            return;
        }

        //gets clipboard and format
        ClipboardFormat clipboardFormat = ClipboardFormat.findByFile(schematicFile);
        ClipboardReader clipboardReader;

        //reads clipboard
        try {
            clipboardReader = clipboardFormat.getReader(Files.newInputStream(schematicFile.toPath()));
        } catch (Exception e) {
            LOGGER.warn("Could not get reader for clipboard: " + arenaID + " | " + e);
            return;
        }

        //reference world
        World worldDataWorld = new BukkitWorld(MAINWORLD);

        //reads the clipboard
        try {
            this.clipboard = clipboardReader.read(worldDataWorld.getWorldData());
        } catch (IOException e) {
            LOGGER.warn("Could not read clipboard: " + arenaID + " | " + e);
            return;
        }

    }

    public void createWorld() {

        //creates temp world
        WorldCreator worldCreator = new WorldCreator("TEMP-ARENA_" + UUID.randomUUID());

        worldCreator.type(WorldType.FLAT);
        worldCreator.generatorSettings("0");
        worldCreator.generateStructures(false);

        world = Bukkit.createWorld(worldCreator);

    }

    public void generateArena() {

        //tests if world is created
        if (world == null) {
            LOGGER.warn("Please start the world before generating the arena");
            return;
        }

        //sets location world
        locationList.forEach(v -> {
            v.setWorld(world);
        });

        //where the arena will start from (center = 0)
        Vector to = new Vector(locationList.get(0).getX(), locationList.get(0).getY(), locationList.get(0).getZ());

        //creates the worldedit world
        World weWorld = new BukkitWorld(world);

        //creates the session and extent
        EditSession extent = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
        ForwardExtentCopy copy = new ForwardExtentCopy(clipboard, clipboard.getRegion(), clipboard.getOrigin(), extent, to);

        //tries to copy
        try {
            Operations.completeLegacy(copy);
        } catch (Exception ignored) {
            LOGGER.warn("Could not copy arena: " + arenaID + " | " + ignored);
        }

        //flushes queue
        extent.flushQueue();
    }

    public void deleteArena() {

        if (world == null || locationList.get(0).getWorld() == MAINWORLD ) {
            LOGGER.warn("Arena not started yet / arena cannot delete main world " + arenaID);
            return;
        }

        //kicks all players from spawn
        locationList.get(0).getWorld().getPlayers().forEach(v -> {
            v.getInventory().clear();
            v.getActivePotionEffects().clear();
            v.teleport(SPAWNLOCATION);
        });

        //deletes the worlds
        deleteTempWorld(locationList.get(0).getWorld().getName(), false);

        activeArenas.remove(uuid);
    }

    public String getArenaID() {
        return arenaID;
    }
    public UUID getUUID() {
        return uuid;
    }
    public ConfigurationSection getSettings() {
        return settings;
    }
    public Clipboard getClipboard() {
        return clipboard;
    }
    public File getSchematicFile() {
        return schematicFile;
    }
    public List<Location> getLocationList() {
        return locationList;
    }
}
