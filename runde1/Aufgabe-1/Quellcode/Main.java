package de.flo.A1_stoerung;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Main class of this Program
 */
public final class Main {

    /**
     * The symbol representing a variable in a sentence format
     */
    public static String VARIABLE = "_";

    /**
     * Constant containing the charset's name used for the scanner since umlauts could be used
     */
    public static String CHARSET_NAME = "UTF-8";

    /**
     * Private constructor, such that no instances of the Main class can be created.
     */
    private Main() {
    }

    /**
     * The program's main function
     *
     * @param args The arguments
     */
    public static void main(String... args) {
        Scanner scanner = new Scanner(System.in, CHARSET_NAME);

        // Get the text's filename from the user
        String message1 = "Please input the filename (or path) of the file containing the text.";
        String textInput = getConsoleInput(scanner, message1);

        // Try to read the text from the file where new lines are replaces with spaces and set it into lower case
        String text;

        try {
            text = getText(textInput);
        } catch (IOException e) {
            System.out.println("Could not read the text!");
            return;
        }

        text = text.toLowerCase(Locale.ROOT);

        // Convert the text into a list of words (splitting at 1 or more spaces)
        List<String> words = getList(text.split("\\s+"));

        // Set of all characters of the text
        Set<Character> allCharacters = Arrays.stream(text.split("")).map(s -> s.charAt(0)).collect(Collectors.toSet());
        // Set of all characters of the text that are letters or digits
        Set<Character> lettersAndDigits = allCharacters.stream().filter(Character::isLetterOrDigit).collect(Collectors.toSet());

        boolean stop = false;

        while (!stop) {
            // Get the user input (filename/path or stop)
            String message2 = "Please input the filename (or path) of the file containing the sentence format. Please enter stop, to stop the program.";
            String taskInput = getConsoleInput(scanner, message2);

            if (taskInput.equalsIgnoreCase("stop")) {
                stop = true;
                continue;
            }

            // Try to get the sentence format from the file
            String sentenceFormat;

            try {
                sentenceFormat = getText(taskInput);
            } catch (IOException e) {
                System.out.println("Could not load the sentence format from the file \"" + taskInput + "\"! Please try again:");
                continue;
            }

            // Get all matching sentences from the list of words and print them
            System.out.println("-------------");
            System.out.println("Input: " + taskInput);

            getAllMatches(sentenceFormat, words, lettersAndDigits).forEach(System.out::println);

            System.out.println("-------------");
        }
    }

    /**
     * Function for getting all sentences (list of words joined by spaces) (of a list of words that match a given sentence format,
     * where any character that can occur in a word is defined by a given Set.)
     *
     * @param format         The (sentence) format
     * @param words          The list of words
     * @param wordCharacters A set of characters allowed in a word
     * @return All subsets of the list of words as sentence
     */
    private static List<String> getAllMatches(String format, List<String> words, Set<Character> wordCharacters) {
        List<String> matches = new ArrayList<>();
        String[] formatArray = format.split(" ");

        // Go through the list of words and check if the current words and the next words match the sentence format
        for (int i = 0; i < words.size() - formatArray.length + 1; i++) {
            // Keep track of the current word in the list with j
            int j = i;
            boolean match = true;

            // Go through the "words" of the sentence format
            for (String formatWord : formatArray) {
                String word = words.get(j);

                // Check if the current word (list at index j) matches the current format-word. This is if
                // 1) The current format word is a variable ("_")
                // 2) The current word is of the form (not-a-word + current format word + no-a-word)
                if (formatWord.equals(VARIABLE) && containsCharacters(word, wordCharacters)) { // 1)
                    j++;
                } else if (matches(word, formatWord, wordCharacters)) { // 2)
                    j++;
                } else { // Otherwise, it's not a match
                    match = false;
                    break;
                }
            }

            // Add the current sublist as a sentence (joined by spaces) to all matches
            if (match) {
                matches.add(String.join(" ", words.subList(i, j)));
            }
        }

        return matches;
    }

    /**
     * Function checking weather a word contains any character of a given set
     *
     * @param word       The word
     * @param characters The set of characters
     * @return If the word contains at least one character contained in the given set
     */
    private static boolean containsCharacters(String word, Set<Character> characters) {
        for (char ch : word.toCharArray()) {
            if (characters.contains(ch)) return true;
        }
        return false;
    }

    /**
     * Function for checking if a string (s) matches another one (c). That is the case if s is of the form
     * a_1ca_2 where a_1 and a_2 are words that only contain characters that are NOT in a given set of characters
     *
     * @param string     The string s
     * @param contained  The string c
     * @param characters The set of permitted characters
     * @return If s is of the correct form.
     */
    private static boolean matches(String string, String contained, Set<Character> characters) {
        // Return false if c isn't even contained in s
        if (!string.contains(contained)) return false;

        // Get s without c and check if the leftover characters are not contained in the given set
        string = string.replaceFirst(contained, "");

        for (char ch : string.toCharArray()) {
            if (characters.contains(ch)) return false;
        }

        return true;
    }

    /**
     * Function for getting a file's text where newlines are spaces.
     *
     * @param fileName The file's name
     * @return The file's line separated spaces (instead of newlines)
     * @throws FileNotFoundException if the file could not be found
     */
    private static String getText(String fileName) throws IOException {
        List<String> lines = getLines(fileName);

        return String.join(" ", lines);
    }

    /**
     * Function reading a file's lines
     *
     * @param fileName The file's name
     * @return A list of the file's lines
     * @throws FileNotFoundException if the file could not be found
     */
    private static List<String> getLines(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        File file = new File(fileName);

        Scanner scanner = new Scanner(file, CHARSET_NAME);
        while (scanner.hasNext()) {
            lines.add(scanner.nextLine());
        }

        return lines;
    }

    /**
     * Function for creating a list, given an array
     *
     * @param array The array
     * @param <T>   The type of the array and the list
     * @return The list based on the array
     */
    private static <T> List<T> getList(T[] array) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, array);
        return list;
    }

    /**
     * Function printing a message onto the console and reading a one line input
     *
     * @param scanner A Scanner
     * @param out     Message printed
     * @return Console's input line
     */
    private static String getConsoleInput(Scanner scanner, String out) {
        System.out.println(out);
        return scanner.nextLine();
    }
}