package de.flo.JA1_reimerei;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Final main class of this program
 */
public final class Main {

    /**
     * String containing all allowed vowels
     */
    public static final String VOWELS = "aeiouöäüéy";

    /**
     * Constant containing the charset's name used for the scanner since umlauts could be used
     */
    public static String CHARSET_NAME = "UTF-8";

    /**
     * Set containing all allowed vowels, created using the VOWELS String
     */
    public static final Set<Character> VOWELS_SET = getCharacters(VOWELS);

    /**
     * Private constructor, such that no instances of the Main class can be created
     */
    private Main() {
    }

    /**
     * The program's main function
     *
     * @param args The arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, CHARSET_NAME);
        boolean stop = false;

        while (!stop) {
            // Get the user input (filename/path or stop)
            String message = "Please input the filename (or path) of the file containing the list of words. Please enter stop, to stop the program.";
            String input = getConsoleInput(scanner, message);

            if (input.equalsIgnoreCase("stop")) {
                stop = true;
                continue;
            }

            // Try to get the words from the file
            List<String> words;

            try {
                words = readWords(input);
            } catch (FileNotFoundException e) {
                System.out.println("Could not load the words from the file \"" + input + "\"! Please try again:");
                continue;
            }

            // Stream the words such that they are all lower case and don't contain spaces
            words = words.stream().map(s -> s.toLowerCase(Locale.ROOT).replace(" ", "")).collect(Collectors.toList());

            // Get all rhyming pairs and print them
            List<Pair<String, String>> pairs = getPairs(words);

            System.out.println("----------");
            System.out.printf("Input File: %s%n", input);
            System.out.printf("Found %d rhyming pairs:%n", pairs.size());

            pairs.forEach(System.out::println);

            System.out.println("----------");
        }
    }

    /**
     * Get all pairs of words from a list of words that rhyme with each other
     *
     * @param words The list of words
     * @return The list of pairs
     */
    public static List<Pair<String, String>> getPairs(List<String> words) {
        // Create and fill HashMap mapping from the ending of a word to a list of words having that ending
        // and can rhyme with any other word (= contain a main vowel group that is together with the rest of
        // the word at least half of it)
        Map<String, List<String>> map = new HashMap<>();

        for (String word : words) {
            // Get the vowel group for the current word (as first index incl, last index excl.)
            Pair<Integer, Integer> mainVowelGroup = getMainVowelGroup(word);

            // If there is no main vowel group, the word cannot rhyme at all. Hence, is not added to the HashMap
            if (mainVowelGroup == null) continue;

            // Check weather the amount of symbols in main vowel group + the rest (to the right) is at least half of the word's length
            // If this isn't true, don't add the word to the HashMap since it cannot rhyme with any word
            int size = word.length() - mainVowelGroup.getFirst();
            boolean canBeUsed = size >= word.length() / 2D;
            if (!canBeUsed) continue;

            // Substring containing main vowel group + rest of the current word
            String ending = word.substring(mainVowelGroup.getFirst());

            // Put the word in its list in the HashMap, create the list if it doesn't exist
            if (!map.containsKey(ending)) map.put(ending, new ArrayList<>());
            map.get(ending).add(word);
        }

        // Create and fill list of all rhyming pairs of words
        List<Pair<String, String>> pairs = new ArrayList<>();

        // Go through all endings (main vowel group + rest) and their corresponding lists
        for (String ending : map.keySet()) {
            List<String> list = map.get(ending);

            // Go through all pairs of words in the current list
            for (int i = 0; i < list.size(); i++) {
                String wordA = list.get(i);

                for (int j = i + 1; j < list.size(); j++) {
                    // Check if one of the words ends with the other one.
                    // If that isn't the case, add the current pair to pairs.
                    String wordB = list.get(j);

                    if (wordA.endsWith(wordB) || wordB.endsWith(wordA)) continue;
                    pairs.add(Pair.of(wordA, wordB));
                }
            }
        }

        return pairs;
    }

    /**
     * Function to get a word's main vowel group given as a pair of the first index (incl.) and the second index (excl.)
     *
     * @param word The word
     * @return An ordered pair containing the start index (incl.) and the end index (excl.) of the main vowel group. If there are no vowels, null is returned.
     */
    private static Pair<Integer, Integer> getMainVowelGroup(String word) {
        int length = word.length();

        // Try to get the first vowel group from the right. If there is none,
        // there cannot be a main vowel group (and null is returned)
        Pair<Integer, Integer> firstVowelGroup = getNextVowelGroupFromRight(word, length);
        if (firstVowelGroup == null) return null;

        // Try to get the second vowel group from the right (to the left of the first one).
        // If it exists, that is the main vowel group. Otherwise, the first one is.
        Pair<Integer, Integer> secondVowelGroup = getNextVowelGroupFromRight(word, firstVowelGroup.getFirst());
        if (secondVowelGroup == null) return firstVowelGroup;
        return secondVowelGroup;
    }

    /**
     * Function to obtain the next vowel group in a word to the left of a given index (going from right to left)
     *
     * @param string The string to find the next vowel group in
     * @param index  The start index (plus one)
     * @return A pair, containing the start index (incl.) and the end index (excl.) of the main vowel group.
     * If there are no vowels to the left of the index, null is returned.
     */
    private static Pair<Integer, Integer> getNextVowelGroupFromRight(String string, int index) {
        // Starting at index-1!
        index--;

        // Go to the left starting at (i-1) until finding a vowel or being out of range (< 0)
        while (index >= 0 && !isVowel(string.charAt(index))) {
            index--;
        }

        // Special case: Check if the start of string was reached: there are no vowels in the word to the left of i.
        if (index == -1) return null;

        // Now, string[index] contains a vowel and (index+1) is the excl. end,
        int endEx = index + 1;

        // Find the start of the vowel group by going through the vowel group until
        // there are no vowels anymore or 0 is reached.
        while (index >= 0 && isVowel(string.charAt(index))) {
            index--;
        }

        // Now, A[index+1] contains a vowel (since A[index] is not a vowel)
        // and hence (index+1) is the incl. end of the vowel group
        int startIn = index + 1;
        return Pair.of(startIn, endEx);
    }

    /**
     * Function to get words from a file (separated by newlines)
     *
     * @param fileName The file's name
     * @return The list of words
     * @throws FileNotFoundException if the File was not found
     */
    private static List<String> readWords(String fileName) throws FileNotFoundException {
        List<String> list = new ArrayList<>();

        File file = new File(fileName);
        Scanner scanner = new Scanner(file, CHARSET_NAME);

        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            list.add(s);
        }

        return list;
    }

    /**
     * Function to get a string's characters as a HashSet
     *
     * @param string The String
     * @return The string's sigle characters as a HashSet
     */
    private static Set<Character> getCharacters(String string) {
        Set<Character> characters = new HashSet<>();

        for (int i = 0; i < string.length(); i++) {
            characters.add(string.charAt(i));
        }

        return characters;
    }

    /**
     * Function to check if a character is a vowel based on VOWELS_SET
     *
     * @param character The character
     * @return If the character is a vowel
     */
    private static boolean isVowel(char character) {
        return VOWELS_SET.contains(character);
    }

    /**
     * Function printing a message onto the console and reading a one line input
     * using a Scanner
     *
     * @param scanner A Scanner
     * @param message Message printed
     * @return The next line from the Scanner
     */
    private static String getConsoleInput(Scanner scanner, String message) {
        System.out.println(message);
        return scanner.nextLine();
    }
}