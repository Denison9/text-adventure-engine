package adventure.commands;

import adventure.engine.GameState;
import adventure.model.Exit;
import adventure.model.Item;
import adventure.model.Openable;
import adventure.model.Player;
import adventure.model.Room;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Αποθηκεύει την κατάσταση του παιχνιδιού σε savegame.json.
// Σώζει: τρέχον δωμάτιο, inventory, flags, ιστορικό εντολών,
// και την κατάσταση κάθε δωματίου (items, πόρτες, exits).
public class SaveCommand implements Command {
    private static final String SAVE_FILE = "savegame.json";
    private final GameState state;

    // Dependency Injection: Η εντολή λαμβάνει το GameState κατά τη δημιουργία της,
    // ώστε να έχει πρόσβαση στην κεντρική ζωντανή μνήμη του παιχνιδιού.
    public SaveCommand(GameState state) {
        this.state = state;
    }

    @Override
    public void execute(Player player, String[] words) {
        try {
            // Δημιουργία του ριζικού DTO (Data Transfer Object) που θα γίνει JSON.
            SaveData data = new SaveData();
            data.currentRoomId = player.getCurrentRoom().getId();
            data.flags = state.getFlags();

            // Defensive copy: Δημιουργία νέας λίστας (snapshot) για την ασφαλή αποθήκευση,
            // ώστε να μην επηρεάζεται από τρέχουσες αλλαγές στο GameState.
            data.commandHistory = new ArrayList<>(state.getCommandHistory());

            // Inventory του παίκτη (αποθηκεύεται ως λίστα από name+description).
            // Μετατρέπουμε τα πολύπλοκα αντικείμενα Item σε απλά ItemData
            data.inventory = new ArrayList<>();
            for (Item item : player.getInventory()) {
                SaveData.ItemData id = new SaveData.ItemData();
                id.name = item.getName();
                id.description = item.getDescription();
                id.readableText = item.getReadableText();
                data.inventory.add(id);
            }

            // Κατάσταση κάθε δωματίου.
            // Σαρώνει ένα ένα τα δωμάτια για να καταγράψουμε το Τοπικό τους State.
            data.roomStates = new HashMap<>();
            for (Room room : state.getRooms()) {
                SaveData.RoomSaveData rsd = new SaveData.RoomSaveData();

                // Items που βρίσκονται στο δωμάτιο αυτή τη στιγμή.
                rsd.items = new ArrayList<>();
                for (Item item : room.getItemsInRoom()) {
                    SaveData.ItemData id = new SaveData.ItemData();
                    id.name = item.getName();
                    id.description = item.getDescription();
                    id.readableText = item.getReadableText();
                    rsd.items.add(id);
                }

                // Κατάσταση ανοιχτό/κλειστό για κάθε openable.
                rsd.openables = new HashMap<>();
                for (Map.Entry<String, Openable> entry : room.getOpenables().entrySet()) {
                    rsd.openables.put(entry.getKey(), entry.getValue().isOpen());
                }

                // Κατάσταση κλειδωμένο/ξεκλείδωτο για κάθε exit.
                rsd.exits = new HashMap<>();
                for (Map.Entry<String, Exit> entry : room.getExits().entrySet()) {
                    rsd.exits.put(entry.getKey(), entry.getValue().isLocked());
                }

                data.roomStates.put(room.getId(), rsd);
            }

            // Serialization
            // Χρήση του GsonBuilder με setPrettyPrinting για να είναι το παραγόμενο JSON
            // ευανάγνωστο (human-readable) και εύκολο στο debugging.
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (Writer writer = new FileWriter(SAVE_FILE)) {
                gson.toJson(data, writer);
            }
            System.out.println("Game saved successfully to '" + SAVE_FILE + "'.");

        } catch (Exception e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    //Inner classes (Data Transfer Objects για το JSON)
    // Αυτές οι κλάσεις είναι 'static' ώστε να είναι εντελώς ανεξάρτητες από το SaveCommand
    // και να μπορεί η βιβλιοθήκη Gson να τις κατασκευάσει/διαβάσει χωρίς προβλήματα.
    // Λειτουργούν αυστηρά ως "καλούπια" δεδομένων, χωρίς καμία επιχειρησιακή λογική.

    static class SaveData {
        String currentRoomId;
        List<ItemData> inventory;
        Map<String, Boolean> flags;
        List<String> commandHistory;
        Map<String, RoomSaveData> roomStates;

        static class ItemData {
            String name;
            String description;
            String readableText;
        }

        static class RoomSaveData {
            List<ItemData> items;
            Map<String, Boolean> openables; // name -> isOpen
            Map<String, Boolean> exits;     // direction -> isLocked
        }
    }
}
