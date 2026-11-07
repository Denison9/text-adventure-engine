package adventure.commands;

import adventure.engine.GameState;
import adventure.model.Condition;
import adventure.model.Exit;
import adventure.model.Item;
import adventure.model.Openable;
import adventure.model.Player;
import adventure.model.Room;

public class UseCommand implements Command {
    private GameState state;

    public UseCommand(GameState state) {
        this.state = state;
    }

    @Override
    public void execute(Player player, String[] words) {
        if (words == null || words.length == 0) {
            System.out.println("What do you want to use?");
            return;
        }

        // Αν ο παίκτης έδωσε δύο ορίσματα (π.χ. "use key on door"), το αντιμετωπίζουμε
        // ως multi-object εντολή: χρήση ενός αντικειμένου ΠΑΝΩ σε έναν στόχο.
        // Έτσι καλύπτεται το παράδειγμα της εκφώνησης "use key on door" και είναι
        // ισοδύναμο με "unlock door with key" από πλευράς αποτελέσματος.
        if (words.length >= 2) {
            executeOnTarget(player, words[0], words[1]);
            return;
        }

        // Μονή χρήση: "use crown" -- διατηρεί την υπάρχουσα λογική νίκης.
        String itemName = words[0];

        if (!player.hasItem(itemName)) {
            System.out.println("You don't have '" + itemName + "' in your inventory.");
            return;
        }

        Room currentRoom = player.getCurrentRoom();

        if (currentRoom.isWinningRoom() && itemName.equalsIgnoreCase(currentRoom.getRequiredItemToWin())) {
            System.out.println("You use the " + itemName + ", something clicks into place...");
            state.setFlag("WON", true);
        } else {
            System.out.println("You can't seem to use the " + itemName + " here.");
        }
    }

    // Χειρισμός "use <item> on <target>". Δουλεύει και αν ο χρήστης δώσει τα ορίσματα
    // με αντίστροφη σειρά (π.χ. "use door with key") επειδή ψάχνουμε ποιο από τα δύο
    // βρίσκεται στο inventory.
    private void executeOnTarget(Player player, String first, String second) {
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

        // 1) Δοκιμάζουμε openable με αυτό το όνομα (π.χ. πόρτα, σεντούκι).
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
            System.out.println("You use the " + itemName + " on the " + openable.getName() + ". It opens!");
            return;
        }

        // 2) Δοκιμάζουμε direct exit (π.χ. "use key on north").
        Exit exit = room.getExit(targetName);
        if (exit != null) {
            if (!exit.isLocked()) {
                System.out.println("The exit to the " + targetName + " is already unlocked.");
                return;
            }
            exit.setLocked(false);
            System.out.println("You use the " + itemName + " on the exit to the " + targetName + ". Click!");
            return;
        }

        // 3) Ειδική περίπτωση νίκης: "use crown on throne" σε winning room.
        if (room.isWinningRoom() && itemName.equalsIgnoreCase(room.getRequiredItemToWin())) {
            System.out.println("You use the " + itemName + ", something clicks into place...");
            state.setFlag("WON", true);
            return;
        }

        System.out.println("You can't use the " + itemName + " on '" + targetName + "' here.");
    }
}
