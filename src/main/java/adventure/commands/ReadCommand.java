package adventure.commands;

import adventure.model.Item;
import adventure.model.Player;

public class ReadCommand implements Command {
    @Override
    public void execute(Player player, String[] words) {
        if (words == null || words.length == 0) {
            System.out.println("What do you want to read?");
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
            System.out.println("There is nothing called '" + itemName + "' here to read.");
            return;
        }

        if (!item.isReadable()) {
            System.out.println("You can't read the " + item.getName() + ".");
            return;
        }

        System.out.println("You read the " + item.getName() + ":");
        System.out.println("\"" + item.getReadableText() + "\"");
    }
}
