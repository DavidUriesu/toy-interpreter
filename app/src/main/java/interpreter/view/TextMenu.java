package interpreter.view;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public void addCommand(Command command) {
        commands.put(command.getKey(), command);
    }

    private void printMenu() {
        for (Command command : commands.values()) {
            System.out.printf("%4s: %s%n",
                    command.getKey(), command.getDescription());
        }
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            System.out.print("Input the option: ");

            if (!scanner.hasNextLine()) {
                return;
            }

            String option = scanner.nextLine().strip();
            Command command = commands.get(option);

            if (command == null) {
                System.out.println("Invalid option");
            }
            else {
                command.execute();
            }
        }
    }
}
