package au.chival.practice.arena;

import java.util.HashMap;
import java.util.UUID;

public class ArenaManager {

    public static HashMap<UUID, Arena> activeArenas = new HashMap<>();

    public static Arena startArena(String arenaID) {

        Arena arena = new Arena(arenaID);

        activeArenas.put(arena.getUUID(), arena);

        return activeArenas.get(arena.getUUID());
    }

    public static Arena getArena(UUID UUID) {
        return activeArenas.get(UUID);
    }
}
