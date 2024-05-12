import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static MipsUnit emulator;
    // public static Map<String, Integer> labels = new HashMap<>();



    // Main method to handle command line input
    public static void main(String[] args) {

        // Load instructions and labels from file if provided
        if (args.length > 0) {
            loadAssemblyFile(args[0]);
        }

        MipsUnit emulator = new MipsUnit();

        // Run in interactive mode or script mode
        if (args.length > 1) {
            runScriptFile(args[1]);
        } else {
            interactiveMode();
        }
    }

    // Load assembly file
    public static void loadAssemblyFile(String filename) {
        Map<String, Integer> labels = new HashMap<>();
        List<String> instructions = new ArrayList<>();



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

        emulator = new MipsUnit(labels, instructions);
    }

    // Run script file
    public static void runScriptFile(String filename) {
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
    public static void interactiveMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("MIPS Emulator started. Type 'h' for help.");
        boolean keepGoing = true;
        while (keepGoing) {
            System.out.print("mips> ");
            String command = scanner.nextLine().trim();
            keepGoing = executeCommand(command);
        }

        scanner.close();
    }

    // Execute command
    public static boolean executeCommand(String command) {
        if (command.equals("h")) {
            emulator.showHelp();
        } else if (command.equals("d")) {
            emulator.dumpRegisters();
        } else if (command.equals("s")) {
            emulator.stepThrough();
        } else if (command.startsWith("s ")) {
            int num = Integer.parseInt(command.substring(2).trim());
            emulator.stepThrough(num);
        } else if (command.equals("r")) {
            emulator.runTheRest();
        } else if (command.startsWith("m ")) {
            String[] parts = command.substring(2).trim().split(" ");
            int num1 = Integer.parseInt(parts[0]);
            int num2 = Integer.parseInt(parts[1]);
            emulator.printMemory(num1, num2);
        } else if (command.equals("c")) {
            emulator.clear();
        } else if (command.equals("q")) {
            return false;
        } else {
            System.out.println("Invalid command. Type 'h' for help.");
        }

        return true;
    }

    // Quit program
    // public void quitProgram() {
    //     System.out.println("Exiting MIPS Emulator.");
    //     System.exit(0);
    // }


}
