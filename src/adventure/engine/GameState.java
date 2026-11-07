package adventure.engine;

import adventure.model.Player;
import adventure.model.Room;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Κρατάει την κατάσταση του παιχνιδιού: παίκτη, όλα τα δωμάτια,
// αν τρέχει, αν νικήθηκε, flags και ιστορικό εντολών.
public class GameState {
    private Player player;
    private List<Room> rooms;
    private boolean isRunning;
    private boolean hasWon;
    private Map<String, Boolean> flags = new HashMap<>();

    // Ιστορικό εντολών χρήστη για υποστήριξη rollback ή καταγραφής (Command History)
    private List<String> commandHistory = new ArrayList<>();

    public GameState(Player player, List<Room> rooms) {
        this.player = player;
        this.rooms = rooms;
        this.isRunning = true;
        this.hasWon = false;
        flags.put("WON", false);
    }

    // Βάζει ένα flag. Όταν είναι το WON, τερματίζει το παιχνίδι.
    public void setFlag(String name, boolean value) {
        flags.put(name, value);
        if (name.equalsIgnoreCase("WON") && value) {
            this.hasWon = true;
        }
    }

    public boolean getFlag(String name) {
        return flags.getOrDefault(name, false);
    }

    // Επιστρέφει αντίγραφο των flags (για Save).
    public Map<String, Boolean> getFlags() {
        return new HashMap<>(flags);
    }

    // Αντικαθιστά τα flags (για Load).
    public void setFlags(Map<String, Boolean> newFlags) {
        flags.clear();
        flags.putAll(newFlags);
        if (Boolean.TRUE.equals(flags.get("WON"))) {
            this.hasWon = true;
        }
    }

    // Προσθέτει εντολή στο ιστορικό.
    public void addToHistory(String command) {
        commandHistory.add(command);
    }

    public List<String> getCommandHistory() { return commandHistory; }

    // Αντικαθιστά το ιστορικό (για Load).
    public void setCommandHistory(List<String> history) {
        commandHistory.clear();
        commandHistory.addAll(history);
    }

    // Βρίσκει δωμάτιο με βάση το ID του (χρησιμοποιείται στο Load).
    public Room getRoomById(String id) {
        for (Room room : rooms) {
            if (room.getId().equals(id)) return room;
        }
        return null;
    }

    public void setRunning(boolean running) { this.isRunning = running; }
    public boolean isRunning() { return isRunning; }
    public boolean isHasWon() { return hasWon; }
    public Player getPlayer() { return player; }
    public List<Room> getRooms() { return rooms; }

    // Καλείται όταν ο παίκτης γράφει quit/exit ή νικάει.
    public void stopGame() {
        this.isRunning = false;
    }
}
