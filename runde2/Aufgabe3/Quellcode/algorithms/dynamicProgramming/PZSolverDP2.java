package de.flo.pancakeSort.algorithms.dynamicProgramming;

import de.flo.pancakeSort.PZSolver;
import de.flo.pancakeSort.util.IntArray;
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
 * A(S).
 * This algorithm is a concrete implementation of "Algorithmus-19"
 */
public class PZSolverDP2 extends PZSolver {

    /**
     * Public constructor only taking in the value of n, such that P(n) can be computed.
     *
     * @param n The value n, such that P(n) can be computed.
     */
    public PZSolverDP2(int n) {
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
        Map<IntArray, Integer> dp = new HashMap<>();

        // Start with the size 1
        dp.put(new IntArray(new int[]{1}), 0);

        // Go bottom up, computing the next dp-table using the last one,
        // up to the dp-table for the S' of size n-1.
        for (int N = 2; N < this.n; N++) {
            dp = getNextDP(N, dp);
        }

        // Create the start permutation containing 1, 2, ... , n
        IntArray list = getStartPermutation(this.n);

        // Keep track of the worst permutation found so far and the (smallest) amount of
        // WUE-Operations needed to solve it. That is, the stack (permutation) with the
        // greatest smallest amount of WUE-Operations needed to sort the stack.
        IntArray worstPermutation = (IntArray) list.clone();
        int worstOperations = 0;

        // Use Heap's Algorithmus to iterate through all permutations of the numbers 1, 2, ... , n.
        int[] c = new int[this.n];
        int i = 1;

        while (i < this.n) {
            if (c[i] < i) {
                list.swap(i % 2 == 0 ? 0 : c[i], i);
                c[i]++;
                i = 1;

                // Check current permutation S by computing its value A(S) using the dp-table
                int currentNumber = getOperationNumber(list, dp);

                // If the smallest amount of WUE-Operations needed to sort the
                // current permutation (currentNumber) is greater than the
                // greatest found so far, update the worst permutation and its length.
                if (currentNumber > worstOperations) {
                    worstPermutation = (IntArray) list.clone();
                    worstOperations = currentNumber;
                }
            } else {
                c[i] = 0;
                i += 1;
            }
        }


        // Return the worst permutation and its length in a pair.
        return new Pair<>(worstOperations, worstPermutation.getArray());
    }

    /**
     * Private methode computing the next dp-table, that will contain all A(S) for permutations
     * S of the size n, using the dp-table containing the results A(S') of all stacks of the size n-1
     *
     * @param n The size of the permutations
     * @param dp The dp-table containing the results A(S') of all stacks of the size n-1
     * @return The nex dp-table containing all A(S) for permutations of the size n
     */
    private Map<IntArray, Integer> getNextDP(int n, Map<IntArray, Integer> dp) {
        // Initialize the next dp-table (HashMap)
        Map<IntArray, Integer> nextDP = new HashMap<>();

        // Put the first (sorted) permutation into the next dp-table
        IntArray list = getStartPermutation(n);

        IntArray firstPermutation = (IntArray) list.clone();
        nextDP.put(firstPermutation, 0);

        // Heap-Algorithmus to iterate through all permutations
        int[] c = new int[n];
        int i = 1;

        // Use Heap's Algorithmus to iterate through all permutations of the numbers 1, 2, ... , n.
        while (i < n) {
            if (c[i] < i) {
                list.swap(i % 2 == 0 ? 0 : c[i], i);
                c[i]++;
                i = 1;

                // Check current permutation S by first copying it
                // and then computing its value A(S) using the dp-table
                IntArray currentPermutation = (IntArray) list.clone();
                int currentNum = getOperationNumber(currentPermutation, dp);

                // Next put the result into the next dp-table
                nextDP.put(currentPermutation, currentNum);
            } else {
                c[i] = 0;
                i += 1;
            }
        }

        return nextDP;
    }

    /**
     * Private methode computing A(S) for a given permutation S of size n using the values
     * of a dp-table containing the A(S') results for permutations of size n-1
     *
     * @param permutation The given permutation S
     * @param dp          The dp-table containing the A(S') results for permutations of size n-1
     * @return The value A(S) for the given permutation S
     */
    private int getOperationNumber(IntArray permutation, Map<IntArray, Integer> dp) {
        // Keep track of the best number of operations
        int min = -1;

        // Iterate through all possible indexes for WUE-Operations on the current stack
        for (int i = 0; i < permutation.getLength(); i++) {
            // Apply the improved WUE-Operation on the given array (stack) using the current index,
            // receiving a new permutation S' and get A(S') using the dp-table
            IntArray currentPermutation = Utils.improvedWUEOperation(permutation, i);
            int currentNumber = dp.get(currentPermutation) + 1;

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
    private IntArray getStartPermutation(int n) {
        IntArray list = new IntArray(n);

        for (int i = 1; i <= n; i++) {
            list.set(i - 1, i);
        }

        return list;
    }
}