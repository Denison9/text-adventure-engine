package adventure.commands;

import adventure.engine.GameState;
import adventure.model.Player;

public class QuitCommand implements Command {
    private final GameState state;

    public QuitCommand(GameState state) {
        this.state = state;
    }

    @Override
    public void execute(Player player, String[] words) {
        System.out.println("Goodbye!");
        state.stopGame();
    }
}
