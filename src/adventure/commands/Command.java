package adventure.commands;

import adventure.model.Player;

public interface Command {
    void execute(Player player, String[] words);
}
