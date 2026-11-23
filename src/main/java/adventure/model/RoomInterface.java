package adventure.model;

import java.util.ArrayList;

public interface RoomInterface {
    void addItem(Item item);
    void removeItem(Item item);
    void setExit(String direction, Room targetRoom, boolean isLocked);
    Exit getExit(String direction);
    String getDescription();
    ArrayList<Item> getItemsInRoom();
    void execute(Player player);
    Item getItem(String itemName);
}
