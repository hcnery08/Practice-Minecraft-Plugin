package au.chival.practice.commands;

import au.chival.practice.arena.Arena;
import au.chival.practice.arena.ArenaManager;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TestCommand extends CommandBase {

    public TestCommand() {
        super("test", false, null);
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) { try {

        Arena arena = ArenaManager.startArena("test");

        } catch (Exception e) { sender.sendMessage(String.valueOf(e)); } }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String[] args) {
        return Collections.emptyList();
    }
}
