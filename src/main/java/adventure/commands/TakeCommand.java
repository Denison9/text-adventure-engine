package adventure.commands;

import adventure.model.Item;
import adventure.model.Player;
import adventure.model.Room;

public class TakeCommand implements Command {
    @Override
    public void execute(Player player, String[] words) {
        if (words == null || words.length == 0) {
            System.out.println("What do you want to take?");
            return;
        }

        String itemName = words[words.length - 1];
        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.getItem(itemName);
        if (item == null && words.length > 1) {
            itemName = words[0];
            item = currentRoom.getItem(itemName);
        }

        if (item != null) {
            currentRoom.removeItem(item);
            player.addItem(item);
            System.out.println("You picked up: " + item.getName());
        } else {
            System.out.println("There is no '" + itemName + "' here.");
        }
    }
}
