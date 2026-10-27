package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.Repository;

import java.util.Scanner;

public class TextMenu {
    private final Scanner scanner = new Scanner(System.in);
    private IStmt selectedProgram;

    public void run() {
        boolean running = true;

        while (running) {
            displayMenu();

            if (!scanner.hasNextLine()) {
                return;
            }

            String option = scanner.nextLine().trim();
            switch (option) {
                case "1" -> selectProgram();
                case "2" -> runSelectedProgram();
                case "0" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void displayMenu() {
        System.out.println();
        System.out.println("1. Select a program");
        System.out.println("2. Run selected program");
        System.out.println("0. Exit");

        if (selectedProgram != null) {
            System.out.println("Selected: " + selectedProgram);
        }
        System.out.println("Option: ");
    }

    private void selectProgram() {
        System.out.println("1. int v; v = 2; print(v)");
        System.out.println("2. int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)");
        System.out.println("3. bool a; int v; a = true; " + "if a then v = 2 else v = 3; print(v)");

        if (!scanner.hasNextLine()) {
            return;
        }

        String option = scanner.nextLine().trim();
        switch (option) {
            case "1" -> selectedProgram = Examples.example1();
            case "2" -> selectedProgram = Examples.example2();
            case "3" -> selectedProgram = Examples.example3();
            default -> {
                System.out.println("Invalid program. Selection unchanged.");
                return;
            }
        }
        System.out.println("Program selected.");
    }

    private void runSelectedProgram() {
        if (selectedProgram == null) {
            System.out.println("Select a program first.");
            return;
        }

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                selectedProgram
        );

        Controller controller = new Controller(new Repository(state), true);

        try {
            controller.allStep();
            System.out.println("Final output: " + state.getOut());
        } catch(MyException error) {
            System.out.println("Interpreter error: " + error.getMessage());
        }
    }
}
