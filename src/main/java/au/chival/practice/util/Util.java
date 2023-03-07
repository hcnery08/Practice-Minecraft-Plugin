package au.chival.practice.util;

import java.io.File;

import static au.chival.practice.Main.JAVAPLUGIN;
import static net.minecraft.server.v1_8_R3.MinecraftServer.LOGGER;

public class Util {

    public static void deleteTempWorlds() {

        File directory = JAVAPLUGIN.getServer().getWorldContainer();

        LOGGER.warn(directory);

        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isDirectory() && file.getName().startsWith("TEMP")) {
                deleteFolder(file);
            }
        }
    }

    public static void deleteTempWorld(String worldName, boolean force) {

        if (!force) {
            if (JAVAPLUGIN.getServer().getWorld(worldName) == null) {
                LOGGER.info("World " + worldName + " does not exist (According to bukkit) | Please set in 'deleteTempWorld(String worldName, boolean force)' boolean force to 'true'");
                return;
            }
        }

        File directory = JAVAPLUGIN.getServer().getWorldContainer();

        LOGGER.warn(directory);

        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isDirectory() && file.getName().equals(worldName)) {
                deleteFolder(file);
            }
        }
    }

    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    file.delete();
                }
            }
        }
        folder.delete();
    }
}
