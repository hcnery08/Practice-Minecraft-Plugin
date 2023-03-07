package au.chival.practice.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

import static au.chival.practice.Main.JAVAPLUGIN;
import static net.minecraft.server.v1_8_R3.MinecraftServer.LOGGER;

public class FileConfig {

    private static HashMap<String, FileConfiguration> fileConfigurations = new HashMap<>();
    private static HashMap<String, File> files = new HashMap<>();

    public static void copyConfig(String fileName) {

        File file = new File(JAVAPLUGIN.getDataFolder(), fileName);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            JAVAPLUGIN.saveResource(fileName, false);
        }

        try {
            config.load(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        files.put(fileName, file);
        fileConfigurations.put(fileName, config);

    }

    public static FileConfiguration getConfig(String fileName) {
        return fileConfigurations.get(fileName);
    }

    public static void saveConfig(String fileName) {

        try {
            fileConfigurations.get(fileName).save(files.get(fileName));
        } catch (Exception e) {
            LOGGER.warn(" ");
            LOGGER.warn("ERROR SAVING " + fileName);
            e.printStackTrace();
            LOGGER.warn(" ");
        }

    }
}