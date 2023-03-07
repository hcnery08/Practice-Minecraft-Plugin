package au.chival.practice.util;

import java.io.File;

import static au.chival.practice.Main.JAVAPLUGIN;

public class FileManager {

    public static void copyFile(String path) {

        File dataFolder = JAVAPLUGIN.getDataFolder();
        File file = new File(JAVAPLUGIN.getDataFolder() + File.separator + path);

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        if (!file.exists()) {
            JAVAPLUGIN.saveResource(path, false);
        }

    }
}
