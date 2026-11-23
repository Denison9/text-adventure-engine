package adventure.commands;

import adventure.model.Item;
import adventure.model.Player;

public class InventoryCommand implements Command {
    @Override
    public void execute(Player player, String[] words) {
        if (player.getInventory().isEmpty()) {
            System.out.println("Your backpack is empty.");
        } else {
            System.out.println("You are carrying:");
            for (Item item : player.getInventory()) {
                System.out.println("  - " + item.getName() + ": " + item.getDescription());
            }
        }
    }
}
