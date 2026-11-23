package adventure.commands;

import adventure.model.Item;
import adventure.model.Player;

public class InspectCommand implements Command {
    @Override
    public void execute(Player player, String[] words) {
        if (words == null || words.length == 0) {
            System.out.println("What do you want to inspect?");
            return;
        }

        String itemName = words[words.length - 1];
        Item item = player.getItem(itemName);
        if (item == null) {
            item = player.getCurrentRoom().getItem(itemName);
        }
        if (item == null && words.length > 1) {
            itemName = words[0];
            item = player.getItem(itemName);
            if (item == null) {
                item = player.getCurrentRoom().getItem(itemName);
            }
        }

        if (item == null) {
            System.out.println("You don't see any '" + itemName + "' here.");
            return;
        }

        System.out.println("You carefully examine: " + item.getName());
        System.out.println(item.getDescription());
    }
}
