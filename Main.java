import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    // Main method to handle command line input
    public static void main(String[] args) {
        MipsUnit emulator = new MipsUnit();

        // Load instructions and labels from file if provided
        if (args.length > 0) {
            emulator.loadAssemblyFile(args[0]);
        }

        // Run in interactive mode or script mode
        if (args.length > 1) {
            emulator.runScriptFile(args[1]);
        } else {
            emulator.interactiveMode();
        }
    }

    // Load assembly file
    public void loadAssemblyFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    if (line.endsWith(":")) {
                        labels.put(line.substring(0, line.length() - 1), instructions.size());
                    } else {
                        instructions.add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Run script file
    public void runScriptFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                executeCommand(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Interactive mode
    public void interactiveMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("MIPS Emulator started. Type 'h' for help.");
        while (true) {
            System.out.print("mips> ");
            String command = scanner.nextLine().trim();
            executeCommand(command);
        }
    }

    // Execute command
    public void executeCommand(String command) {
        if (command.equals("h")) {
            showHelp();
        } else if (command.equals("d")) {
            dumpRegisters();
        } else if (command.equals("s")) {
            stepThrough();
        } else if (command.startsWith("s ")) {
            int num = Integer.parseInt(command.substring(2).trim());
            stepThrough(num);
        } else if (command.equals("r")) {
            runTheRest();
        } else if (command.startsWith("m ")) {
            String[] parts = command.substring(2).trim().split(" ");
            int num1 = Integer.parseInt(parts[0]);
            int num2 = Integer.parseInt(parts[1]);
            printMemory(num1, num2);
        } else if (command.equals("c")) {
            clear();
        } else if (command.equals("q")) {
            quitProgram();
        } else {
            System.out.println("Invalid command. Type 'h' for help.");
        }
    }

    // Quit program
    public void quitProgram() {
        System.out.println("Exiting MIPS Emulator.");
        System.exit(0);
    }

    // Clears registers, data memory, and sets pc back to 0
    public void clear() {
        for (String reg : registers.keySet()) {
            registers.put(reg, 0);
        }

        // Clear memory
        Arrays.fill(dataMemory, 0);

        // Pc back to 0
        programCounter = 0;

        System.out.print("\tsimulator reset\n");
    }
    // Step through one instruction in the program
    public void stepThrough() {
        stepThrough(1);
    }

    // Step through n instructions in the program
    public void stepThrough(int numSteps) {
        for (int i = 0; i < numSteps; i++) {
            executeLine();
        }
        System.out.printf("%d instruction(s) executed\n", numSteps);
    }

    // Run until program ends
    public void runTheRest() {
        while (programCounter < instructions.size()) {
            stepThrough();
        }
    }

    // Display data memory between two locations (inclusive)
    public void printMemory(int num1, int num2) {
        for (int i = num1; i <= num2; i++) {
            System.out.printf("[%d] = %d\n", i, dataMemory[i]);
        }
    }

}
