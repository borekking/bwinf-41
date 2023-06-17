package de.flo.pancakeSort;

import de.flo.pancakeSort.algorithms.dynamicProgramming.PZSolverDP2;
import de.flo.pancakeSort.algorithms.dynamicProgramming.PZSolverDP3;
import de.flo.pancakeSort.algorithms.dynamicProgramming.PancakeSortDP;
import de.flo.pancakeSort.util.Pair;
import de.flo.pancakeSort.util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Final main class of this program containing the main-function
 */
public final class Main {

    /**
     * Private constructor, sucht that no instances of this class can be created
     */
    private Main() {
    }

    /**
     * This program's main function
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        // Define a scanner for the console that will be used for all user inputs
        Scanner scanner = new Scanner(System.in);
        boolean run = true; // Run while-loop as long as run is true

        while (run) {
            // Get an integer input by the user: 0 for stopping the program, 1 for running a Pancake Sorter, 2 for computing a P(n)
            int program = getInputInt(scanner, "Enter the program you want to use (0 -> Stop programm; 1 -> WUE-Operations-Folge; 2 -> P(n)):");

            switch (program) {
                // If the given program is equal to 0, stop the program
                case 0: {
                    System.out.println("Stopping programm...");
                    run = false; // Stop the while-loop by setting run to false
                    break;
                }
                // If the given integer is 1, run a Pancake Sorter
                case 1: {
                    runStackSolver(scanner);
                    break;
                }
                // If the given integer 2, run a P(n) computer
                case 2: {
                    runPWUENumber(scanner);
                    break;
                }
                // If the inputted integer is neither 0, 1 nor 2, print an error message and continue the loop
                default: {
                    System.out.println("Not a correct number! Please try again.");
                    break;
                }
            }
        }
    }

    /**
     * Private static function getting a pancake stack from the given
     * scanner and starting a Stack Solver (PancakeSort).
     *
     * @param scanner The scanner scanning the console that is used for all user inputs
     */
    private static void runStackSolver(Scanner scanner) {
        // Get an input int using the given scanner:
        // 0 for stopping the program, 1 for proving the stack in a file and finally 2 for entering the stack manually
        int program = getInputInt(scanner, "Enter if you want to enter to stack manually or via a filename (0 -> Stop programm; 1 -> File; 2 -> Custom):");

        // Int array that will contain the given pancake stack
        int[] array;

        switch (program) {
            // If the input is 0, return.
            case 0: {
                return;
            }
            // If the input is 1, try to get and read a file and convert its content to the int array
            case 1: {
                // Get file name or path and check for its existence. If it does not exist, print an error message and return.
                String path = getInput(scanner, "Please enter a filename of a file containing the stack: ");
                if (!fileNameExists(path)) {
                    System.out.println("Error occurred - Filename \"" + path + "\" did not exist!  Please try again.");
                    return;
                }

                // Try to get the file's content using the given path.
                // Print an error message and return if an FileNotFoundException occurs.
                File file = new File(path);
                List<String> fileContent;

                try {
                    fileContent = readFile(file);
                } catch (FileNotFoundException e) {
                    System.out.println("Error occurred - Filename \"" + path + "\" did not exist! Please try again.");
                    return;
                }

                // Check if the file's content has the correct format. E.i., every line contains only one or
                // more digits (without spaces) and the file is not empty. Otherwise, print an error message and return.
                boolean allIntegers = fileContent.stream().allMatch(s -> s.matches("\\d+"));

                if (fileContent.isEmpty() || !allIntegers) {
                    System.out.println("Error occurred - File dit not have correct format!");
                    return;
                }

                // Next, convert the list containing the file's content into an int array (without the first line)
                int n = fileContent.size();
                array = fileContent.subList(1, n).stream().mapToInt(Integer::parseInt).toArray();
                break;
            }
            case 2: {
                // Get
                String stackString = getInput(scanner, "Please enter the stack using the following format: <s_1> ... <s_i> ... <s_n>");

                // Check if input has the correct format. E.i., consists of one line starting with one or more
                // digits followed by some amount of (spaces followed by one or more digit).
                // Otherwise, print an error message and return.
                boolean allIntegers = stackString.matches("\\d+(\\s\\d+)*");

                if (!allIntegers) {
                    System.out.println("Error occurred - Input dit not have correct format!");
                    return;
                }

                // Convert the given input into an int array
                array = Arrays.stream(stackString.split(" ")).mapToInt(Integer::parseInt).toArray();
                break;
            }
            // If the input wasn't 0, 1 or 2, print an error message and return
            default: {
                System.out.println("Error occurred - Not a correct number! Please try again.");
                return;
            }
        }

        // After an int array was created using the user inputs (from the file or scanner), check if the
        // given stack is actually a permutation. If this is not the case, print an error message and return.
        if (!Utils.isPermutation(array)) {
            System.out.println("Error occurred - Not a permutation: " + Arrays.toString(array));
            return;
        }

        // Now, actually start solving the problem for the given stack using a PancakeSort implementation by
        // calling the run methode with the PancakeSort instance.
        PancakeSort solver = new PancakeSortDP(array);
        run(solver, array);
    }

    /**
     * Private static function solving the MWO-Problem using a Pancake Solver, e.i.,
     * computing the min. amount of WUE-Operations for the given stack and also given
     * an example.
     *
     * @param solver The solver used to solve the problem
     * @param array  The pancake stack
     */
    private static void run(PancakeSort solver, int[] array) {
        System.out.println("--------------------");
        System.out.println("Starting..."); // Print that the program will now start solving

        // Solve the problem using the given solver and stop the time in ms
        long milli = System.currentTimeMillis();
        int[] result = solver.solve();
        milli = System.currentTimeMillis() - milli;

        // Print the Flipping Operations and their application on the given stack
        int[] stack = array.clone();
        System.out.println();
        System.out.println("Flipping Operations:");
        System.out.println(Arrays.toString(stack));

        for (int i : result) {
            stack = Utils.standardWUEOperation(stack, i);
            System.out.println(i + " -> " + Arrays.toString(stack));
        }

        // Print the actual results:
        // First the given stack and the resulting stack, as well as the used flipping Operations and the time needed
        System.out.println();
        System.out.printf("Pancakes: %s (%d) -> %s (%d)%n", Arrays.toString(array), array.length, Arrays.toString(stack), stack.length);
        System.out.printf("Flipping Operations:  %s (%d)%n", Arrays.toString(result), result.length);
        System.out.printf("Time: %dms%n", milli);
        System.out.println("--------------------");
    }

    /**
     * Private static function starting a P(n) Solver (PZSolver)
     *
     * @param scanner The scanner scanning the console that is used for all user inputs
     */
    private static void runPWUENumber(Scanner scanner) {
        // Get an integer input (the value n) from the user using the given scanner
        int n = getInputInt(scanner, "Please enter n, such that P(n) can be computed:");

        // Make sure n >= 1
        if (n <= 0) {
            System.out.println("Error occurred - n cannot be less than 1!");
            return;
        }

        // Use a different PZSolver for different n.
        // If n is less than or equal to 15, use the PZSolverDP3 that used
        // an optimized hashing function working for n <= 15.
        // Otherwise, use PZSolver2 using the default hashing of IntArray
        PZSolver solver;

        if (n <= 15) {
            solver = new PZSolverDP3(n);
        } else {
            solver = new PZSolverDP2(n);
        }

        // Actually start computing and print the result using the run function
        run(solver, n);
    }

    /**
     * Private static function solving the PWZ-Problem using a PZ Solver, e.i.,
     * computing P(n) for a given n using a given solver.
     *
     * @param solver The solver used to solve the problem
     * @param n      The value n given to the solver before
     */
    private static void run(PZSolver solver, int n) {
        System.out.println("--------------------");
        System.out.println("Starting..."); // Print that the program will now start solving

        // Solve the problem using the given solver and stop the time in ms
        long milli = System.currentTimeMillis();
        Pair<Integer, int[]> result = solver.solve();
        milli = System.currentTimeMillis() - milli;

        // Get the actual result (max - P(n), array - an example)
        int max = result.first();
        int[] array = result.second();

        // Now, print the P(n) value max and the corresponding example
        // as well as the needed time in ms.
        System.out.printf("Result : P(%d) = %d%n", n, max);
        System.out.printf("Example: %s%n", Arrays.toString(array));
        System.out.printf("Time   : %dms%n", milli);
        System.out.println("--------------------");
    }

    /**
     * Private static function printing a given message into the console and
     * returning a one-line console input from the user
     *
     * @param scanner A scanner scanning the console
     * @param message The message for the user
     * @return A one-line console input from the user
     */
    private static String getInput(Scanner scanner, String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    /**
     * Private static function printing a given message onto the console and returning
     * an integer inputted by the user. This function loops until the
     * user actually inputs an integer and prints an error message otherwise.
     *
     * @param scanner A scanner scanning the console
     * @param message The message for the user
     * @return The inputted integer
     */
    private static int getInputInt(Scanner scanner, String message) {
        // Init result to zero even though the loop will run until an actual value is given by the user
        int result = 0;
        boolean loop = true; // While loop is true, the loop below keeps running

        while (loop) {
            // Get the next line inputted by the user
            String input = getInput(scanner, message);

            // Try to parse the given input string into an integer. If that is
            // possible, the while-loop is stopped and result is set to be that integer.
            // Otherwise, an error message is printed and the loop keeps running.
            try {
                result = Integer.parseInt(input);
                loop = false;
            } catch (NumberFormatException e) {
                System.out.println("Error occurred - Not an integer: \"" + input + "\". Please try again.");
            }
        }

        // Finally, return the given integer
        return result;
    }

    /**
     * Private static function returning if a given filename or path is valid.
     * That is, iff the file or path exists and is a file (not a dictionary).
     *
     * @param path The filename or path
     * @return {@code true} if the file exists and is a normal file, {@code false} otherwise
     */
    private static boolean fileNameExists(String path) {
        File file = new File(path);
        return file.isFile(); // pathname exists and is a normal file
    }

    /**
     * Static function reading a file line by line and returning a list of strings containing those lines.
     *
     * @param file The file
     * @return The file's lines
     * @throws FileNotFoundException If the file is not found
     */
    private static List<String> readFile(File file) throws FileNotFoundException {
        List<String> list = new ArrayList<>();
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            list.add(scanner.nextLine());
        }

        return list;
    }
}