package adventure.commands;

import adventure.model.Exit;
import adventure.model.Player;
import adventure.model.Room;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Ξανατυπώνει την κατάσταση του τρέχοντος δωματίου: όνομα, περιγραφή,
// items στο δάπεδο, openables, και τις διαθέσιμες εξόδους.
// Είναι χρήσιμο όταν η οθόνη έχει γεμίσει από άλλες εντολές (inventory,
// history κλπ) και ο παίκτης χρειάζεται να ξαναδεί τον χώρο.
public class LookCommand implements Command {
    @Override
    public void execute(Player player, String[] words) {
        Room currentRoom = player.getCurrentRoom();
        System.out.println("--- " + currentRoom.getName() + " ---");
        System.out.println(currentRoom.getDescription());
        currentRoom.execute(player);

        // Λίστα διαθέσιμων εξόδων με ένδειξη (locked) όπου χρειάζεται.
        Map<String, Exit> exits = currentRoom.getExits();
        if (!exits.isEmpty()) {
            List<String> directions = new ArrayList<>();
            for (Map.Entry<String, Exit> entry : exits.entrySet()) {
                String dir = entry.getKey();
                if (entry.getValue().isLocked()) {
                    directions.add(dir + " (locked)");
                } else {
                    directions.add(dir);
                }
            }
            System.out.println("Exits: " + String.join(", ", directions));
        } else {
            System.out.println("There are no visible exits.");
        }
    }
}
