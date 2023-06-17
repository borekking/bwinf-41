package de.flo.pancakeSort.algorithms.betterWUEOperation;

import de.flo.pancakeSort.PZSolver;
import de.flo.pancakeSort.util.Pair;
import de.flo.pancakeSort.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the abstract class PZSolver that can find
 * P(n) for a given natural number n and a corresponding example stack
 * of the given size that needs at least P(n) WUE-Operations to be
 * converted into a sorted stack.
 * This algorithm works by computing A(S) for each possible permutation of the
 * given size n using Heap's Algorithm and is a concrete implementation of "Algorithmus-10" in the documentation
 * using "Algorithmus-15" for getting A(S).
 */
public class PZSolver3 extends PZSolver {

    /**
     * Public constructor only taking in the value of n, such that P(n) can be computed.
     *
     * @param n The value n, such that P(n) can be computed.
     */
    public PZSolver3(int n) {
        super(n);
    }

    /**
     * Implementation of the solve-methode solving the PWZ-Problem as described above.
     *
     * @return A pair containing P(n) and a corresponding example stack
     */
    @Override
    public Pair<Integer, int[]> solve() {
        // Create the start permutation containing 1, 2, ... , n
        int[] list = new int[this.n];
        for (int i = 1; i <= this.n; i++) {
            list[i - 1] = i;
        }

        // Keep track of the worst permutation found so far and the (smallest) amount of
        // WUE-Operations needed to solve it. That is, the stack (permutation) with the
        // greatest smallest amount of WUE-Operations needed to sort the stack.
        int[] worstPermutation = list.clone();
        int worstOperations = 0;

        // Use Heap's Algorithmus to iterate through all permutations of the numbers 1, 2, ... , n.
        int[] c = new int[this.n];
        int i = 1;

        while (i < this.n) {
            if (c[i] < i) {
                Utils.swap(list, i % 2 == 0 ? 0 : c[i], i);
                c[i]++;
                i = 1;

                // Check the current permutation S by first copying it and then
                // computing its value A(S)
                int currentNumber = getA(list);

                // If the smallest amount of WUE-Operations needed to sort the
                // current permutation (currentNumber) is greater than the
                // greatest found so far, update the worst permutation and its length.
                if (currentNumber > worstOperations) {
                    worstPermutation = list.clone();
                    worstOperations = currentNumber;
                }
            } else {
                c[i] = 0;
                i += 1;
            }
        }

        // Return the worst permutation and its length in a pair.
        return new Pair<>(worstOperations, worstPermutation);
    }

    /**
     * Recursive private methode for finding A(S) for a given stack S.
     *
     * @param permutation The given stack S
     * @return The value of A(S)
     */
    private int getA(int[] permutation) {
        // Go through all possible sizes of index series.
        // Note that the size (permutation.length - 1 = n-1) always creates a sorted stack.
        for (int m = 0; m < permutation.length - 1; m++) {
            // Initialize the current list of indexes
            List<Integer> list = new ArrayList<>();

            // Recursively test out every possible list of indexes of the current size m.
            // If the methode returns true, a list of the current size m was found, such
            // that m can be returned.
            if (solveRecursively(list, m, permutation)) {
                return m;
            }
        }

        // If none of the sizes before worked, return (permutation.length - 1 = n-1)
        // since this size always creates a sorted stack.
        return permutation.length - 1;
    }

    /**
     * Private methode for recursively trying out every possible list of indexes of a given size m
     * used for the WUE-Operation and return weather any list of this size can sort a given stack (permutation)
     * when used as a series of WUE-Operations
     *
     * @param list        The current list of indexes
     * @param m           The wanted size for the list of indexes
     * @param permutation The
     * @return true, if a list of indexes of the given size m, converting
     * the given stack into a sorted one, exists. False otherwise.
     */
    private boolean solveRecursively(List<Integer> list, int m, int[] permutation) {
        // If the current list of indexes has reached the wanted size,
        // check if converts the given stack into a sorted one by
        // applying the normal WUE-Operation.
        if (list.size() == m) {
            // Copy the given stack (permutation)
            int[] result = permutation.clone();

            // Apply the WUE-Operations given by the current list of indexes
            for (int index : list) {
                result = Utils.improvedWUEOperation(result, index);
            }

            // Return weather the current list of indexes could sort the stack (permutation)
            return Utils.isSortedPermutation(result);
        }

        // Iterate through all possible indexes (0 to n - list.size() - 1)
        for (int index = 0; index < n - list.size(); index++) {
            // Add the current index to the list of indexes
            list.add((Integer) index);

            // Recursively use this methode to create lists of indexes
            // starting with the current one (or check the current one)
            if (solveRecursively(list, m, permutation)) {
                return true;
            }

            // Remove the last element of the list
            list.remove(list.size() - 1);
        }
        return false;
    }
}