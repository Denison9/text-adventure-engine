package adventure.ui;

import java.util.Scanner;

public class ConsoleImpl implements ConsoleUI {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String getUserInput() {
        System.out.print("> ");
        return scanner.nextLine();
    }
}
