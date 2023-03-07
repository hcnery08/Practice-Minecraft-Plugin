package au.chival.practice.game;

import au.chival.practice.arena.Arena;
import au.chival.practice.arena.ArenaManager;
import au.chival.practice.util.FileConfig;
import net.minecraft.server.v1_8_R3.PlayerAbilities;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static net.minecraft.server.v1_8_R3.MinecraftServer.LOGGER;

public class Game {

    ConfigurationSection section;
    HashMap<Integer, List<Player>> players = new HashMap<>();
    Arena arena;
    //Kit kit;

    public Game(String gameID, HashMap<Integer, List<Player>> players) {

        this.section = FileConfig.getConfig("data.yml").getConfigurationSection("games." + gameID);

        String arenaString = null;

        if (!(section.get("arenas") instanceof List) || !(section.get("arenas") instanceof String)) {
            LOGGER.warn("Game " + gameID + " does not have a valid yml (at arenas:~here)");
            return;
        }

        if (section.get("arenas") instanceof List) {
            List<String> arenaList = (List<String>) section.get("arenas");
            arenaString = arenaList.get(new Random().nextInt(0, arenaList.size() - 1));
        }

        if (section.get("arenas") instanceof String) {
            arenaString = section.getString("arenas");
        }

        this.arena = ArenaManager.startArena(arenaString);

    }

    public void runGame() {

    }

}
