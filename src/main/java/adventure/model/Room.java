package adventure.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Room implements RoomInterface {
    private String id;
    private String name;
    private String description;
    private ArrayList<Item> itemsInRoom;
    private HashMap<String, Exit> exits;
    private HashMap<String, Openable> openables;
    private boolean winningRoom;
    private String requiredItemToWin;

    public Room(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.itemsInRoom = new ArrayList<>();
        this.openables = new HashMap<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override public void addItem(Item item) { this.itemsInRoom.add(item); }
    @Override public void removeItem(Item item) { this.itemsInRoom.remove(item); }

    @Override
    public void setExit(String direction, Room targetRoom, boolean isLocked) {
        this.exits.put(direction, new Exit(direction, targetRoom, isLocked));
    }

    @Override public Exit getExit(String direction) { return exits.get(direction); }
    @Override public String getDescription() { return description; }
    @Override public ArrayList<Item> getItemsInRoom() { return itemsInRoom; }

    public void setWinningRoom(boolean isWinning) { this.winningRoom = isWinning; }
    public boolean isWinningRoom() { return winningRoom; }
    public void setRequiredItemToWin(String item) { this.requiredItemToWin = item; }
    public String getRequiredItemToWin() { return requiredItemToWin; }

    public void addOpenable(Openable openable) {
        this.openables.put(openable.getName().toLowerCase(), openable);
    }

    public Openable getOpenable(String name) {
        return openables.get(name.toLowerCase());
    }

    // Επιστρέφει όλα τα openables (χρησιμοποιείται στο Save/Load).
    public Map<String, Openable> getOpenables() { return openables; }

    // Επιστρέφει όλες τις εξόδους (χρησιμοποιείται στο Save/Load).
    public Map<String, Exit> getExits() { return exits; }

    // Αδειάζει τα items του δωματίου (χρησιμοποιείται στο Load).
    public void clearItems() { itemsInRoom.clear(); }

    @Override
    public void execute(Player player) {
        if (!itemsInRoom.isEmpty()) {
            System.out.print("On the floor you see: ");
            for (Item item : itemsInRoom) {
                System.out.print(item.getName() + "  ");
            }
            System.out.println();
        } else {
            System.out.println("There is nothing notable on the floor.");
        }
        if (!openables.isEmpty()) {
            System.out.print("You also notice: ");
            for (Openable o : openables.values()) {
                System.out.print(o.getName() + " (" + (o.isOpen() ? "open" : "closed") + ")  ");
            }
            System.out.println();
        }
    }

    @Override
    public Item getItem(String itemName) {
        for (Item item : itemsInRoom) {
            if (item.getName().equalsIgnoreCase(itemName)) return item;
        }
        return null;
    }
}
