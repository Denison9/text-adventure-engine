package adventure.commands;

import adventure.model.Condition;
import adventure.model.Openable;
import adventure.model.Player;

public class OpenCommand implements Command {
    @Override
    public void execute(Player player, String[] words) {
        if (words == null || words.length == 0) {
            System.out.println("What do you want to open?");
            return;
        }

        String targetName = words[words.length - 1];
        Openable target = player.getCurrentRoom().getOpenable(targetName);

        if (target == null) {
            System.out.println("There is no '" + targetName + "' here to open.");
            return;
        }
        if (target.isOpen()) {
            System.out.println("The " + target.getName() + " is already open.");
            return;
        }

        Condition condition = target.getUnlockCondition();
        if (condition != null && !condition.isMet(player)) {
            System.out.println(condition.getFailMessage());
        } else {
            target.setOpen(true);
            System.out.println("You opened the " + target.getName() + "!");
        }
    }
}
