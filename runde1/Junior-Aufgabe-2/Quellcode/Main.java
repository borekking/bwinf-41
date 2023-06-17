package de.flo.JA2_container;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Final main class of this program
 */
public final class Main {

    /**
     * Private Constructor, such that no instances of the Main class can be created
     */
    private Main() {
    }

    /**
     * The program's main function
     *
     * @param args The arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean stop = false;

        while (!stop) {
            // Get the user input (filename/path or stop)
            String message = "Please input the filename (or path) of the file containing the list of pairs of containers. Please enter stop, to stop the program";
            String input = getConsoleInput(scanner, message);

            if (input.equalsIgnoreCase("stop")) {
                stop = true;
                continue;
            }

            // Try to load the pairs (of containers) from the file
            List<Pair<Integer, Integer>> pairs;

            try {
                pairs = getPairs(input);
            } catch (FileNotFoundException e) {
                System.out.println("Could not load the pairs from file \"" + input + "\"! Please try again:");
                continue;
            }

            System.out.println("----------");
            System.out.println("Input File: " + input);

            // Get all containers that might be the heaviest
            Set<Integer> potentialHeaviestContainers = getPotentialHeaviestContainers(pairs);
            int amount = potentialHeaviestContainers.size();

            // Based on the amount of potential heaviest containers, output the solution
            if (amount == 0) {
                System.out.println("Not possible! Error occurred");
            } else if (amount == 1) {
                System.out.println("The heaviest container is " + potentialHeaviestContainers.toArray()[0]);
            } else {
                System.out.println("No heaviest container! Potential heaviest containers: " + potentialHeaviestContainers);
            }

            System.out.println("----------");
        }
    }

    /**
     * Function for getting all containers that are potentially the heaviest, given a list of pairs of containers
     * where the first container is heavier than the second one
     *
     * @param pairs Given pairs (c_1, c_2) of containers where c_1 > c_2
     * @return A HashSet of potential heaviest containers
     */
    private static Set<Integer> getPotentialHeaviestContainers(List<Pair<Integer, Integer>> pairs) {
        // Fill HashSets with 1) all containers 2) containers on the right (of the pairs)
        Set<Integer> allContainers = new HashSet<>(); // 1)
        Set<Integer> rightSights = new HashSet<>(); // 2)

        for (Pair<Integer, Integer> pair : pairs) {
            int a = pair.getFirst();
            int b = pair.getSecond();

            allContainers.add(a);
            allContainers.add(b);

            rightSights.add(b);
        }

        // Create a new HashSet containing all containers and remove all containers that are on any right side,
        // such that the new HashSet contains all containers that are never on a right side and hence cannot be
        // lighter than any other container
        Set<Integer> biggest = new HashSet<>(allContainers);
        biggest.removeAll(rightSights);

        return biggest;
    }

    /**
     * Function reading all pairs of numbers (one pair per line) from a file
     *
     * @param fileName The file's name
     * @return The pairs from the file
     * @throws FileNotFoundException if the file couldn't be loaded
     */
    private static List<Pair<Integer, Integer>> getPairs(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        List<Pair<Integer, Integer>> pairs = new ArrayList<>();

        // For each line: Get the two numbers of the pair
        while (scanner.hasNext()) {
            String[] arr = scanner.nextLine().split(" ");
            pairs.add(Pair.of(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])));
        }

        return pairs;
    }

    /**
     * Function printing a message onto the console and reading a one line input
     * using a Scanner
     *
     * @param scanner A Scanner
     * @param out     Message printed
     * @return The next line from the Scanner
     */
    private static String getConsoleInput(Scanner scanner, String out) {
        System.out.println(out);
        return scanner.nextLine();
    }
}