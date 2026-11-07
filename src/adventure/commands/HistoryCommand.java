package adventure.commands;

import adventure.engine.GameState;
import adventure.model.Player;
import java.util.List;

// Τυπώνει αριθμημένη λίστα με τις εντολές που έδωσε ο παίκτης ως τώρα.
public class HistoryCommand implements Command {
    private final GameState state;

    public HistoryCommand(GameState state) {
        this.state = state;
    }

    @Override
    public void execute(Player player, String[] words) {
        List<String> history = state.getCommandHistory();
        if (history.isEmpty()) {
            System.out.println("No commands in history yet.");
            return;
        }
        System.out.println("Command History:");
        for (int i = 0; i < history.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + history.get(i));
        }
    }
}
