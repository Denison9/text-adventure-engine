package adventure.commands;

import adventure.engine.GameState;
import adventure.model.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, Command> commands = new HashMap<>();
    private CommandParser parser;

    public CommandRegistry(CommandParser parser, GameState state) {
        this.parser = parser;

        register("go", new GoCommand());
        register("look", new LookCommand());
        register("inventory", new InventoryCommand());
        register("take", new TakeCommand());
        register("drop", new DropCommand());
        register("inspect", new InspectCommand());
        register("open", new OpenCommand());
        register("unlock", new MultiObjectCommand());
        register("use", new UseCommand(state));
        register("read", new ReadCommand());
        register("history", new HistoryCommand(state));
        register("save", new SaveCommand(state));
        register("load", new LoadCommand(state));
        Command quit = new QuitCommand(state);
        register("quit", quit);
        register("exit", quit);
    }

    public void register(String commandName, Command command) {
        commands.put(commandName.toLowerCase(), command);
    }

    public void dispatch(String input, Player player) {
        CommandParser.ParsedData parsedData = parser.parse(input);

        if (parsedData == null) return;

        String verb = parsedData.getVerb();
        Command command = commands.get(verb);

        if (command != null) {
            command.execute(player, parsedData.getArguments());
        } else {
            List<String> known = new ArrayList<>(commands.keySet());
            Collections.sort(known);
            System.out.println("I don't understand '" + verb + "'. Known commands: " + String.join(", ", known) + ".");
        }
    }
}
