package adventure.commands;

import adventure.model.Exit;
import adventure.model.Player;
import adventure.model.Room;

public class GoCommand implements Command {
    @Override
    public void execute(Player player, String[] words) {
        if (words == null || words.length == 0) {
            System.out.println("Where do you want to go?");
            return;
        }

        String direction = words[0];
        Exit selectedExit = player.getCurrentRoom().getExit(direction);

        if (selectedExit != null) {
            if (selectedExit.isLocked()) {
                System.out.println("The exit to the " + direction + " is locked!");
                return;
            }
            Room nextRoom = selectedExit.getTargetRoom();
            if (nextRoom != null) {
                player.setCurrentRoom(nextRoom);
                System.out.println("You go " + direction + ".");
                System.out.println("--- " + nextRoom.getName() + " ---");
                System.out.println(nextRoom.getDescription());
                nextRoom.execute(player);
            }
        } else {
            System.out.println("You can't go that way!");
        }
    }
}
