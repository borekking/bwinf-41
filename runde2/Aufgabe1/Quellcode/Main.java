package de.flo.wenigerKrummeTouren;

import de.flo.wenigerKrummeTouren.algorithms.exact.ExactSolver4;
import de.flo.wenigerKrummeTouren.algorithms.greedy.GreedySolver3;
import de.flo.wenigerKrummeTouren.util.Point;
import de.flo.wenigerKrummeTouren.util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
            // Get an integer user input
            int dimension = getInputInt(scanner, "Please enter the input an dimension (natural number) or -1 if you want to stop the programm: ");

            // If the given dimension is equal to -1, stop the program
            if (dimension == -1) {
                System.out.println("Stopping programm...");
                run = false; // Stop the while-loop by setting run to false
                continue;
            }

            // Check if the given dimension is valid, meaning that it's greater than 0.
            if (!isValidDimension(dimension)) {
                System.out.println("A dimension must be greater than 0!  Please try again.");
                continue;
            }

            // Get the file's name of the file containing the points from the user
            String path = getInput(scanner, "Please enter a filename or path of a file containing coordinates of the dimension entered before: ");

            // Check if the file actually exists and print an error message and continue the loop otherwise
            if (!fileNameExists(path)) {
                System.out.println("Error occurred - Filename \"" + path + "\" did not exist!  Please try again.");
                continue;
            }

            // Try to get the file's content using the given path.
            // Print an error message and continue the loop if an FileNotFoundException occurs.
            File file = new File(path);
            List<String> filesContent;

            try {
                filesContent = readFile(file);
            } catch (FileNotFoundException e) {
                System.out.println("Error occurred - Filename \"" + path + "\" did not exist! Please try again.");
                continue;
            }

            // Try to convert the list containing the file's content into a list containing points
            // Checking for errors NPE and NumberFormat, printing an error message and continuing the loop if one occurs
            List<Point> points;
            try {
                points = filesContent.stream()
                        .map(Utils::splitBySpace)
                        .map(array -> Arrays.stream(array).mapToDouble(Double::parseDouble).toArray())
                        .map(array -> new Point(array, false))
                        .collect(Collectors.toList());
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Error occurred - Problem while converting file's content into list of points! Please try again.");
                continue;
            }

            // Check if all points have the same and correct (given) dimension,
            // printing an error message and continuing the loop if this isn't the case
            if (points.stream().anyMatch(point -> point.getDimension() != dimension)) {
                System.out.println("Error occurred - Not all of the points have the given dimension (" + dimension + ")! Please try again.");
                continue;
            }

            // Convert the ArrayList of points into an array of points
            Point[] pointsArray = points.toArray(new Point[0]);

            // Finally, solve the problem using the best exact solver if the amount of points is smaller than 16
            // and using a greedy solver otherwise.
            Solver solver;

            if (points.size() <= 20) {
                solver = new ExactSolver4(pointsArray);
            } else {
                solver = new GreedySolver3(pointsArray);
            }

            // Solve the problem and print the result using the chosen Solver using the run-function
            run(solver);
        }
    }

    /**
     * Private static function for solving the problem given a certain Solver
     * and print the results
     *
     * @param solver The solver used to solve the problem
     */
    private static void run(Solver solver) {
        System.out.println("-------------");
        System.out.println("Starting..."); // Print that the program will now start solving

        // Solve the problem using the given solver and stop the time in ms
        long milli = System.currentTimeMillis();
        Point[] result = solver.solve();
        milli = System.currentTimeMillis() - milli;

        // If the result is null (no result was found), print a message saying so,
        // otherwise just print the result line by line (one point per line)
        if (result == null) {
            System.out.println("RESULT IS NULL");
        } else {
            System.out.println("Result:");

            for (Point point : result) {
                System.out.println(point);
            }

            System.out.println();
        }

        // Now, print the routs length and the time needed for solving the instance
        System.out.println("Length = " + (result == null ? -1 : Utils.length(result)));
        System.out.println("Time: " + milli + "ms");
        System.out.println("-------------");
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
     * Private static function returning if a given integer is a valid dimension (for a point).
     * That is, iff the integer is greater than or equal to one.
     *
     * @param n The dimension
     * @return If the dimension is valid
     */
    private static boolean isValidDimension(int n) {
        return n >= 1;
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