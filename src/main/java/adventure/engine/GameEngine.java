package adventure.engine;

import adventure.commands.CommandRegistry;
import adventure.model.Room;
import adventure.ui.ConsoleUI;

// Ο οδηγός του παιχνιδιού.
//Αναλαμβάνει το κεντρικό Game Loop, διαβάζοντας την είσοδο του χρήστη
//και προωθώντας την στο CommandRegistry προς εκτέλεση.
public class GameEngine {
    private GameState state;
    private CommandRegistry registry;
    private ConsoleUI ui;

    public GameEngine(GameState state, CommandRegistry registry, ConsoleUI ui) {
        this.state = state;
        this.registry = registry;
        this.ui = ui;
    }

    public void run() {

        // Τυπώνει το πρώτο δωμάτιο μία φορά πριν αρχίσει το loop.
        describeCurrentRoom();
        //Game Loop
        while (state.isRunning()) {
            String input = ui.getUserInput();

            // Όταν ο χρήστης πατάει Ctrl-D ή κλείνει το stream, σταματάει.
            if (input == null) {
                state.stopGame();
                continue;
            }

            // Αποθηκεύει την εντολή στο ιστορικό (μόνο αν δεν είναι κενή).
            if (!input.trim().isEmpty()) {
                state.addToHistory(input.trim());
            }

            // Στέλνει την εντολή στον Registry για να την εκτελέσει.
            registry.dispatch(input, state.getPlayer());
            // Αν κάποια εντολή έβαλε το flag WON, νικάει ο παίκτης.
            if (state.isHasWon()) {
                ui.printMessage("Congratulations! You won the game!");
                state.stopGame();
                continue;
            }
            ui.printMessage("");
        }
    }

    // Τυπώνει όνομα δωματίου, περιγραφή και ό,τι υπάρχει μέσα.
    private void describeCurrentRoom() {
        Room currentRoom = state.getPlayer().getCurrentRoom();
        ui.printMessage("--- " + currentRoom.getName() + " ---");
        ui.printMessage(currentRoom.getDescription());
        currentRoom.execute(state.getPlayer());
        ui.printMessage("");
    }
}
