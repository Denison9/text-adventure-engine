package adventure.commands;

import adventure.engine.GameState;
import adventure.model.Exit;
import adventure.model.Item;
import adventure.model.Openable;
import adventure.model.Player;
import adventure.model.Room;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.Reader;
import java.util.Map;

// Φορτώνει αποθηκευμένη κατάσταση από savegame.json και αποκαθιστά
// τον παίκτη, το inventory, τα δωμάτια, τα flags και το ιστορικό εντολών.
public class LoadCommand implements Command {
    private static final String SAVE_FILE = "savegame.json";
    private final GameState state;

    public LoadCommand(GameState state) {
        this.state = state;
    }

    @Override
    public void execute(Player player, String[] words) {
        try (Reader reader = new FileReader(SAVE_FILE)) {
            Gson gson = new Gson();
            SaveCommand.SaveData data = gson.fromJson(reader, SaveCommand.SaveData.class);

            if (data == null) {
                System.out.println("Save file is empty or corrupted.");
                return;
            }

            // 1) Επαναφορά τρέχοντος δωματίου.
            Room savedRoom = state.getRoomById(data.currentRoomId);
            if (savedRoom == null) {
                System.out.println("Error: saved room '" + data.currentRoomId + "' not found.");
                return;
            }
            player.setCurrentRoom(savedRoom);

            // 2) Επαναφορά inventory παίκτη.
            player.clearInventory();
            if (data.inventory != null) {
                for (SaveCommand.SaveData.ItemData id : data.inventory) {
                    player.addItem(new Item(id.name, id.description, id.readableText));
                }
            }

            // 3) Επαναφορά κατάστασης κάθε δωματίου.
            if (data.roomStates != null) {
                for (Room room : state.getRooms()) {
                    SaveCommand.SaveData.RoomSaveData rsd = data.roomStates.get(room.getId());
                    if (rsd == null) continue;

                    // Items: αδειάζει πρώτα το δωμάτιο, μετά βάζει τα αποθηκευμένα.
                    room.clearItems();
                    if (rsd.items != null) {
                        for (SaveCommand.SaveData.ItemData id : rsd.items) {
                            room.addItem(new Item(id.name, id.description, id.readableText));
                        }
                    }

                    // Openables: επαναφορά ανοιχτό/κλειστό.
                    if (rsd.openables != null) {
                        for (Map.Entry<String, Boolean> entry : rsd.openables.entrySet()) {
                            Openable openable = room.getOpenable(entry.getKey());
                            if (openable != null) {
                                openable.setOpen(entry.getValue());
                            }
                        }
                    }

                    // Exits: επαναφορά κλειδωμένο/ξεκλείδωτο.
                    // Γίνεται ΜΕΤΑ τα openables για να υπερισχύσει σε περίπτωση σύγκρουσης.
                    if (rsd.exits != null) {
                        for (Map.Entry<String, Boolean> entry : rsd.exits.entrySet()) {
                            Exit exit = room.getExit(entry.getKey());
                            if (exit != null) {
                                exit.setLocked(entry.getValue());
                            }
                        }
                    }
                }
            }

            // 4) Επαναφορά flags.
            if (data.flags != null) {
                state.setFlags(data.flags);
            }

            // 5) Επαναφορά ιστορικού εντολών.
            if (data.commandHistory != null) {
                state.setCommandHistory(data.commandHistory);
            }

            System.out.println("Game loaded successfully from '" + SAVE_FILE + "'.");
            System.out.println("--- " + player.getCurrentRoom().getName() + " ---");
            System.out.println(player.getCurrentRoom().getDescription());
            player.getCurrentRoom().execute(player);

        } catch (java.io.FileNotFoundException e) {
            System.out.println("No save file found. Use 'save' first.");
        } catch (Exception e) {
            System.out.println("Error loading game: " + e.getMessage());
        }
    }
}
