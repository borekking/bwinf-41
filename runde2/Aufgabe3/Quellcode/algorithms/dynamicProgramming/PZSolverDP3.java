package de.flo.pancakeSort.algorithms.dynamicProgramming;

import de.flo.pancakeSort.PZSolver;
import de.flo.pancakeSort.util.Pair;
import de.flo.pancakeSort.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the abstract class PZSolver that can find
 * P(n) for a given natural number n and a corresponding example stack
 * of the given size that needs at least P(n) WUE-Operations to be
 * converted into a sorted stack.
 * This algorithm works by computing A(S) for each possible permutation of the
 * given size n using Heap's Algorithm and will use a bottom up Dynamic Programming
 * approach to compute values of A(S). E.i., first compute all permutations of
 * size 1, 2, 3, ... n-1. To then compute the permutations of size n and get the maximum
 * A(S). This algorithmen uses a custom-made hashing function for n <= 15 and is a
 * concrete implementation of "Algorithmus-19".
 */
public class PZSolverDP3 extends PZSolver {

    /**
     * Public constructor only taking in the value of n, such that P(n) can be computed.
     *
     * @param n The value n, such that P(n) can be computed.
     */
    public PZSolverDP3(int n) {
        super(n);
    }

    /**
     * Implementation of the solve-methode solving the PWZ-Problem as described above.
     *
     * @return A pair containing P(n) and a corresponding example stack
     */
    @Override
    public Pair<Integer, int[]> solve() {
        // Compute all values of A(S) where S has the size n-1
        // using dynamic programming, going from the bottom up to n-1
        Map<Long, Integer> dp = new HashMap<>();

        // Start with the size 1
        dp.put(hash(new int[]{1}), 0);

        // Go bottom up, computing the next dp-table using the last one,
        // up to the dp-table for the S' of size n-1.
        for (int N = 2; N < this.n; N++) {
            dp = getNextDP(N, dp);
        }

        // Create the start permutation containing 1, 2, ... , n
        int[] list = getStartPermutation(this.n);

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

                // Check current permutation S by computing its value A(S) using the dp-table
                int currentNumber = getOperationNumber(list, dp);

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
     * Custom hash function computing the hash value for a given permutation of a size n <= 16.
     *
     * @param list The given permutation
     * @return The permutation's hash value
     */
    private long hash(int[] list) {
        int n = list.length;

        // Make sure the permutation's size is smaller than 16.
        if (n >= 16) throw new IllegalArgumentException();

        // Compute the hash by seeing the given permutation as a number
        // with base n and convert it to a decimal number.
        // E.i., computing \sum_{i=0}^{n-1} n^i * A[i] for a permutation A.
        long hash = 0;
        long mul = 1;
        long base = n;

        for (int i : list) {
            hash += mul * i;
            mul *= base;
        }

        // Return the computed hash.
        return hash;
    }

    /**
     * Private methode computing the next dp-table, that will contain all A(S) for permutations
     * S of the size n, using the dp-table containing the results A(S') of all stacks of the size n-1
     *
     * @param n  The size of the permutations
     * @param dp The dp-table containing the results A(S') of all stacks of the size n-1
     * @return The nex dp-table containing all A(S) for permutations of the size n
     */
    private Map<Long, Integer> getNextDP(int n, Map<Long, Integer> dp) {
        // Initialize the next dp-table (HashMap)
        Map<Long, Integer> nextDP = new HashMap<>();

        // Create the start permutation containing 1, 2, ... , n
        int[] list = getStartPermutation(n);

        // Put the first (sorted) permutation into the next dp-table
        int[] firstPermutation = list.clone();
        nextDP.put(hash(firstPermutation), 0);

        // Use Heap's Algorithmus to iterate through all permutations of the numbers 1, 2, ... , n.
        int[] c = new int[n];
        int i = 1;

        while (i < n) {
            if (c[i] < i) {
                Utils.swap(list, i % 2 == 0 ? 0 : c[i], i);
                c[i]++;
                i = 1;

                // Check current permutation S by first copying it
                // and then computing its value A(S) using the dp-table
                int[] currentPermutation = list.clone();
                int currentNum = getOperationNumber(currentPermutation, dp);

                // Next put the result into the next dp-table
                nextDP.put(hash(currentPermutation), currentNum);
            } else {
                c[i] = 0;
                i += 1;
            }
        }

        return nextDP;
    }

    /**
     * Private methode for computing A(S) for a given permutation S of size n using the values
     * of a dp-table containing the A(S') results for permutations of size n-1
     *
     * @param permutation The given permutation S
     * @param dp          The dp-table containing the A(S') results for permutations of size n-1
     * @return The value A(S) for the given permutation S
     */
    private int getOperationNumber(int[] permutation, Map<Long, Integer> dp) {
        // Keep track of the best number of operations
        int min = -1;

        // Iterate through all possible indexes for WUE-Operations on the current stack
        for (int i = 0; i < permutation.length; i++) {
            // Apply the improved WUE-Operation on the given array (stack) using the current index,
            // receiving a new permutation S' and get A(S') using the dp-table by hashing S' manually
            int[] currentPermutation = Utils.improvedWUEOperation(permutation, i);
            int currentNumber = dp.get(hash(currentPermutation)) + 1;

            // If min is still -1 or the current result (currentNumber) is smaller
            // than min, update min to the currentNumber
            if (min == -1 || currentNumber < min) {
                min = currentNumber;
            }
        }

        // Finally, return min
        return min;
    }

    /**
     * Private methode returning the id permutation (1, 2, ... , n)
     *
     * @param n The permutations size
     * @return The id permutation (1, 2, ... , n)
     */
    private int[] getStartPermutation(int n) {
        int[] list = new int[n];
        for (int i = 0; i < n; i++) list[i] = i + 1;
        return list;
    }
}