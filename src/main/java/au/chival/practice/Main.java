package au.chival.practice;

import au.chival.practice.commands.ArenaCommand;
import au.chival.practice.commands.GameCommand;
import au.chival.practice.commands.KitCommand;
import au.chival.practice.commands.TestCommand;
import au.chival.practice.util.FileConfig;
import au.chival.practice.util.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static au.chival.practice.util.Util.deleteTempWorlds;

public final class Main extends JavaPlugin {

    public static JavaPlugin JAVAPLUGIN;

    public static World MAINWORLD;
    public static Location SPAWNLOCATION;

    @Override
    public void onEnable() {
        JAVAPLUGIN = this;
        loadAll();
    }

    public void loadAll() {
        //files
        JAVAPLUGIN.saveDefaultConfig();
        FileManager.copyFile("schematics" + File.separator + ".temp");
        FileConfig.copyConfig("data.yml");

        //cmd-reg
        new TestCommand();
        new ArenaCommand();
        new GameCommand();
        new KitCommand();

        //listener-reg


        //other
        deleteTempWorlds();
        MAINWORLD = Bukkit.getWorld(getConfig().getString("mainWorld"));
        if (getConfig().get("spawnLocation") == null) {
            getConfig().set("spawnLocation", MAINWORLD.getSpawnLocation());
        }
        SPAWNLOCATION = (Location) getConfig().get("spawnLocation");
        JAVAPLUGIN.saveConfig();

    }

    @Override
    public void onDisable() {
        deleteTempWorlds();
    }
}
