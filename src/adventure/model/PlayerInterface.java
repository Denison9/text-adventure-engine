package adventure.model;

import java.util.ArrayList;

public interface PlayerInterface {
    Room getCurrentRoom();
    void setCurrentRoom(Room newRoom);
    void addItem(Item item);
    void removeItem(Item item);
    String getName();
    ArrayList<Item> getInventory();
}
