package adventure.model;

import java.util.ArrayList;

public class Player implements PlayerInterface {
    private String name;
    private Room currentRoom;
    private ArrayList<Item> inventory;

    public Player(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.inventory = new ArrayList<>();
    }

    @Override public Room getCurrentRoom() { return this.currentRoom; }
    @Override public void setCurrentRoom(Room newRoom) { this.currentRoom = newRoom; }
    @Override public void addItem(Item item) { this.inventory.add(item); }
    @Override public void removeItem(Item item) { this.inventory.remove(item); }
    @Override public String getName() { return name; }
    @Override public ArrayList<Item> getInventory() { return this.inventory; }

    // Αδειάζει το inventory (χρησιμοποιείται στο Load).
    public void clearInventory() { inventory.clear(); }

    public boolean hasItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) return true;
        }
        return false;
    }

    public Item getItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) return item;
        }
        return null;
    }
}
