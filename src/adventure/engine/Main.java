package adventure.engine;

import adventure.commands.CommandParser;
import adventure.commands.CommandRegistry;
import adventure.commands.SynonymMap;
import adventure.loader.JsonGameLoader;
import adventure.model.Player;
import adventure.model.Room;
import adventure.ui.ConsoleImpl;
import adventure.ui.ConsoleUI;
import java.util.List;

// Σημείο εκκίνησης. Στήνει όλα τα κομμάτια και ξεκινά το παιχνίδι.
public class Main {
    public static void main(String[] args) {
        // Δέχεται προαιρετικά paths για world και synonyms από command line.
        // Έτσι ο ίδιος engine μπορεί να τρέξει διαφορετικά παιχνίδια χωρίς
        // αλλαγή Java κώδικα (π.χ. java Main world2.json).
        String worldPath;
        if (args.length > 0) {
            worldPath = args[0];
        } else {
            worldPath = "world.json";
        }

        String synonymsPath;
        if (args.length > 1) {
            synonymsPath = args[1];
        } else {
            synonymsPath = "synonyms.json";
        }


        System.out.println("=== Loading Game from " + worldPath + " ===");

        // 1) Φορτώνει τα δωμάτια από JSON.
        JsonGameLoader loader = new JsonGameLoader();
        List<Room> worldRooms = loader.loadRooms(worldPath);

        if (worldRooms == null || worldRooms.isEmpty()) {
            System.err.println("Error: world could not be loaded from " + worldPath + ". Exiting.");
            return;
        }

        // 2) Δημιουργεί παίκτη και κατάσταση. Το πρώτο δωμάτιο είναι το αρχικό.
        // Τα worldRooms περνιούνται στο GameState ώστε να είναι διαθέσιμα για Save/Load.
        Room startingRoom = worldRooms.get(0);
        Player player = new Player("Hero", startingRoom);
        GameState state = new GameState(player, worldRooms);

        // 3) Φορτώνει synonyms και στήνει parser + registry.
        SynonymMap synonymMap = new SynonymMap(synonymsPath);
        CommandParser parser = new CommandParser(synonymMap);
        CommandRegistry registry = new CommandRegistry(parser, state);
        ConsoleUI ui  = new ConsoleImpl();

        // 4) Δημιουργεί τον engine και τον τρέχει.
        GameEngine engine = new GameEngine(state, registry, ui);
        System.out.println("=== Game Start! ===\n");
        engine.run();
    }
}
