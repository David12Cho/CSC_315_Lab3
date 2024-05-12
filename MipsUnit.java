import java.util.*;

public class MipsUnit {

    private Map<String, Integer> labels;
    private List<String> instructions;
    private int programCounter;
    private Map<String, Integer> registers;
    private int[] dataMemory;

    public MipsUnit(Map<String, Integer> labels, List<String> instructions) {
        // Initialize programCounter and dataMemory
        programCounter = 0;
        dataMemory = new int[8192];

        // Initialize label
        this.labels = labels;

        // Initialize instructions
        this.instructions = instructions;

        // Initialize registers
        registers = new HashMap<>();
        registers.put("$0", 0);
        registers.put("$v0", 0);
        registers.put("$v1", 0);
        registers.put("$a0", 0);
        registers.put("$a1", 0);
        registers.put("$a2", 0);
        registers.put("$a3", 0);
        registers.put("$t0", 0);
        registers.put("$t1", 0);
        registers.put("$t2", 0);
        registers.put("$t3", 0);
        registers.put("$t4", 0);
        registers.put("$t5", 0);
        registers.put("$t6", 0);
        registers.put("$t7", 0);
        registers.put("$s0", 0);
        registers.put("$s1", 0);
        registers.put("$s2", 0);
        registers.put("$s3", 0);
        registers.put("$s4", 0);
        registers.put("$s5", 0);
        registers.put("$s6", 0);
        registers.put("$s7", 0);
        registers.put("$t8", 0);
        registers.put("$t9", 0);
        registers.put("$sp", 0);
        registers.put("$ra", 0);
    }

    public MipsUnit() {
        this(new HashMap<String, Integer>(), new ArrayList<String>());
    }

    // Prints out valid commands
    public void showHelp() {
        System.out.print(
                """
                        h = show help
                        d = dump register state
                        s = single step through the program (i.e. execute 1 instruction and stop)
                        s num = step through num instructions of the program
                        r = run until the program ends
                        m num1 num2 = display data memory from location num1 to num2
                        c = clear all registers, memory, and the program counter to 0
                        q = exit the program
                        \n
                        """
        );
    }

    // Set all registers to 0
    public void dumpRegisters() {
        System.out.printf("pc = %d\n", programCounter);

        System.out.printf("$0 = %d\t$v0 = %d\t$v1 = %d\t$a0 = %d\t\n",
                registers.get("$0"),
                registers.get("$v0"),
                registers.get("$v1"),
                registers.get("$a0")
        );

        System.out.printf("$a1 = %d\t$a2 = %d\t$a3 = %d\t$t0 = %d\t\n",
                registers.get("$a1"),
                registers.get("$a2"),
                registers.get("$a3"),
                registers.get("$t0")
        );
        System.out.printf("$t1 = %d\t$t2 = %d\t$t3 = %d\t$t4 = %d\t\n",
                registers.get("$t1"),
                registers.get("$t2"),
                registers.get("$t3"),
                registers.get("$t4")
        );
        System.out.printf("$t5 = %d\t$t6 = %d\t$t7 = %d\t$s0 = %d\t\n",
                registers.get("$t5"),
                registers.get("$t6"),
                registers.get("$t7"),
                registers.get("$s0")
        );
        System.out.printf("$s1 = %d\t$s2 = %d\t$s3 = %d\t$s4 = %d\t\n",
                registers.get("$s1"),
                registers.get("$s2"),
                registers.get("$s3"),
                registers.get("$s4")
        );
        System.out.printf("$s5 = %d\t$s6 = %d\t$s7 = %d\t$t8 = %d\t\n",
                registers.get("$s5"),
                registers.get("$s6"),
                registers.get("$s7"),
                registers.get("$t8")
        );
        System.out.printf("$t9 = %d\t$sp = %d\t$ra = %d\n",
                registers.get("$t9"),
                registers.get("$sp"),
                registers.get("$ra")
        );
    }

    // HELPER FUNCTION: called by stepThrough(int numSteps),
    // but is the one that actually executes an instruction
    public void executeLine() {
        String[] command = instructions.get(programCounter).trim().split("//s+");

        // and, or, add, addi, sll, sub, slt, beq, bne, lw, sw, j, jr, and jal

        switch (command.length) {
            case 4:
                command[1] = command[1].substring(0, command[1].length() - 1);
                command[2] = command[2].substring(0, command[2].length() - 1);

                switch (command[0]) {
                    case "and":
                        and(command[1], command[2], command[3]);
                        break;

                    case "or":
                        or(command[1], command[2], command[3]);
                        break;

                    case "add":
                        add(command[1], command[2], command[3]);
                        break;

                    case "addi":
                        addi(command[1], command[2], command[3]);
                        break;

                    case "sll":
                        sll(command[1], command[2], command[3]);
                        break;

                    case "sub":
                        sub(command[1], command[2], command[3]);
                        break;

                    case "slt":
                        slt(command[1], command[2], command[3]);
                        break;

                    case "beq":
                        beq(command[1], command[2], command[3]);
                        break;

                    case "bne":
                        bne(command[1], command[2], command[3]);
                        break;

                    default:
                        System.out.print("error\n");

                }

            case 3:
                command[1] = command[1].substring(0, command[1].length() - 1);

                switch (command[0]) {
                    case "lw":
                        lw(command[1], command[2]);
                        break;
                    case "sw":
                        sw(command[1], command[2]);
                        break;
                    default:
                        System.out.print("error\n");
                }

            case 2:
                switch (command[0]) {
                    case "j":
                        j(command[1]);
                        break;
                    case "jr":
                        jr(command[1]);
                        break;
                    case "jal":
                        jal(command[1]);
                        break;
                    default:
                        System.out.print("error\n");

                }

            default:
                System.out.print("error\n");
        }

        programCounter++;
    }




    // Step through one instruction in the program
    public  void stepThrough(){
        stepThrough(1);
    }

    // Step through n instructions in the program
    public  void stepThrough(int numSteps){
        for (int i = 0; i < numSteps; i++){
            executeLine();
        }

        System.out.printf("%d instruction(s) executed\n", numSteps);
    }

    // Run until program ends
    public  void runTheRest(){
        while(programCounter < instructions.size()){
            stepThrough();
        }
    }

    // Display data memory between two locations (inclusive)
    public  void printMemory(int num1, int num2){
        for(int i = num1; i <= num2; i++){
            System.out.printf("[%d] = %d\n", i, dataMemory[i]);
        }
        
    }

    // Clears registers, data memory, and sets pc back to 0
    public void clear(){
        for (String reg : registers.keySet()){
            registers.put(reg, 0);
        }

        // Clear memory
        dataMemory = new int[8192];

        // Pc back to 0
        programCounter = 0;

        System.out.print("\tsimulator reset\n");
    }

    // No quit

    /***  ALL SUPPORTED INSTRUCTION COMMANDS  ***/
    // and, or, add, addi, sll, sub, slt, beq, bne, lw, sw, j, jr, and jal


    /* ALL COMMANDS NEEDING THREE PARAMETERS */
    public void and(String arg1, String arg2, String arg3) {
        registers.put("arg1",
                registers.get("arg2") & registers.get("arg3")
        );
    }

    public void or(String arg1, String arg2, String arg3) {
        registers.put("arg1",
                registers.get("arg2") | registers.get("arg3")
        );
    }

    public void add(String arg1, String arg2, String arg3) {
        registers.put("arg1",
                registers.get("arg2") + registers.get("arg3")
        );
    }

    public void addi(String arg1, String arg2, String arg3) {
        registers.put("arg1",
                registers.get("arg2") + Integer.parseInt("arg3")
        );

    }

    public void sll(String arg1, String arg2, String arg3) {
        registers.put("arg1",
                registers.get("arg2") << Integer.parseInt("arg3")
        );
    }

    public void sub(String arg1, String arg2, String arg3) {
        registers.put("arg1",
                registers.get("arg2") - registers.get("arg3")
        );
    }

    public void slt(String arg1, String arg2, String arg3) {
        int zeroFlag = 0;

        if (registers.get("arg2") < registers.get("arg3")) {
            zeroFlag = 1;
        }

        registers.put("arg1",
                zeroFlag
        );

    }

    public void beq(String arg1, String arg2, String arg3) {
        if (registers.get("arg1") == registers.get("arg2")) {
            programCounter = labels.get("arg3");
        }
    }

    public void bne(String arg1, String arg2, String arg3) {
        if (registers.get("arg1") != registers.get("arg2")) {
            programCounter = labels.get("arg3");
        }
    }


    /* ALL COMMANDS NEEDING TWO PARAMETERS */
    public void lw(String arg1, String arg2) {
        int address = offSet(arg2);

        registers.put(arg1, dataMemory[address]);
    }

    public void sw(String arg1, String arg2) {
        int address = offSet(arg2);

        dataMemory[address] = registers.get(arg1);
    }

    // Helper function to offset for data memory
    public int offSet(String arg1) {
        int[] result = new int[2];
        String[] parts = arg1.split("\\$");

        for (int i = 0; i < 2; i++) {
            String number = parts[i].replaceAll("[^0-9]", "");
            result[i] = Integer.parseInt(number);
        }

        return result[0] + result[1];
    }

    /* ALL COMMANDS WITH ONE ARGUMENT */
    public void j(String arg1) {
        programCounter = labels.get(arg1);
    }

    public void jr(String arg1) {
        programCounter = registers.get(arg1);
    }

    public void jal(String arg1) {
        registers.put("$ra", programCounter);
        j(arg1);

    }
}



















