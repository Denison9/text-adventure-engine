package adventure.commands;

import adventure.model.Item;
import adventure.model.Player;

public class DropCommand implements Command {
    @Override
    public void execute(Player player, String[] words) {
        if (words == null || words.length == 0) {
            System.out.println("What do you want to drop?");
            return;
        }

        String itemName = words[words.length - 1];
        Item item = player.getItem(itemName);
        if (item == null && words.length > 1) {
            itemName = words[0];
            item = player.getItem(itemName);
        }

        if (item == null) {
            System.out.println("You don't have '" + itemName + "' in your inventory.");
            return;
        }

        player.removeItem(item);
        player.getCurrentRoom().addItem(item);
        System.out.println("You dropped '" + item.getName() + "' on the floor.");
    }
}
