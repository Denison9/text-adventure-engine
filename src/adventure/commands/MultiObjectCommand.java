package adventure.commands;

import adventure.model.Condition;
import adventure.model.Exit;
import adventure.model.Item;
import adventure.model.Openable;
import adventure.model.Player;
import adventure.model.Room;

public class MultiObjectCommand implements Command {
    @Override
    public void execute(Player player, String[] words) {
        if (words == null || words.length < 2) {
            System.out.println("Usage: unlock <target> with <item>  (e.g. unlock door with key)");
            return;
        }

        String first  = words[0];
        String second = words[1];

        String itemName;
        String targetName;

        if (player.getItem(first) != null) {
            itemName   = first;
            targetName = second;
        } else if (player.getItem(second) != null) {
            itemName   = second;
            targetName = first;
        } else {
            System.out.println("You don't have '" + first + "' or '" + second + "' in your inventory.");
            return;
        }

        Item tool = player.getItem(itemName);
        if (tool == null) {
            System.out.println("You don't have '" + itemName + "' in your inventory.");
            return;
        }

        Room room = player.getCurrentRoom();

        Openable openable = room.getOpenable(targetName);
        if (openable != null) {
            if (openable.isOpen()) {
                System.out.println("The " + openable.getName() + " is already open.");
                return;
            }
            Condition cond = openable.getUnlockCondition();
            if (cond != null && !cond.isMet(player)) {
                System.out.println(cond.getFailMessage());
                return;
            }
            openable.setOpen(true);
            System.out.println("You use the " + itemName + " and unlock the " + openable.getName() + "!");
            return;
        }

        Exit exit = room.getExit(targetName);
        if (exit == null) {
            System.out.println("There is no '" + targetName + "' here to unlock.");
            return;
        }
        if (!exit.isLocked()) {
            System.out.println("The exit to the " + targetName + " is already unlocked.");
            return;
        }
        exit.setLocked(false);
        System.out.println("You use the " + itemName + " and unlock the exit to the " + targetName + "!");
    }
}
