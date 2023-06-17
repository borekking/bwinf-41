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
 * given size n using Heap's Algorithm and will use Dynamic Programming for
 * memorizing result for A(S).
 * This algorithm is a concrete implementation of "Algorithmus-18"
 */
public class PZSolverDP1 extends PZSolver {

    /**
     * HashMap mapping from the int-array wrapper class IntArray
     * to integers. This hashmap contains the results of the
     * getA-methode, such that subproblems that have already been solved are
     * returned instantly.
     */
    private final Map<IntArray, Integer> dp;

    /**
     * Public constructor only taking in the value of n, such that P(n) can be computed.
     *
     * @param n The value n, such that P(n) can be computed.
     */
    public PZSolverDP1(int n) {
        super(n);

        // Initialize the Map used for dynamic programming with an empty HashMap
        this.dp = new HashMap<>();
    }

    /**
     * Implementation of the solve-methode solving the PWZ-Problem as described above.
     *
     * @return A pair containing P(n) and a corresponding example stack
     */
    @Override
    public Pair<Integer, int[]> solve() {
        // Create the start permutation containing 1, 2, ... , n
        IntArray list = new IntArray(this.n);
        for (int i = 1; i <= this.n; i++) {
            list.set(i - 1, i);
        }

        // Keep track of the worst permutation found so far and the (smallest) amount of
        // WUE-Operations needed to solve it. That is, the stack (permutation) with the
        // greatest smallest amount of WUE-Operations needed to sort the stack.
        IntArray worstPermutation = (IntArray) list.clone();
        int worstOperations = getA(worstPermutation);

        // Use Heap's Algorithmus to iterate through all permutations of the numbers 1, 2, ... , n.
        int[] c = new int[this.n];
        int i = 1;

        while (i < this.n) {
            if (c[i] < i) {
                list.swap(i % 2 == 0 ? 0 : c[i], i);
                c[i]++;
                i = 1;

                // Check current permutation S by first copying it
                // and then computing its value A(S)
                IntArray currentPermutation = (IntArray) list.clone();
                int currentNum = getA(currentPermutation);

                // If the smallest amount of WUE-Operations needed to sort the
                // current permutation (currentNumber) is greater than the
                // greatest found so far, update the worst permutation and its length.
                if (currentNum > worstOperations) {
                    worstPermutation = currentPermutation;
                    worstOperations = currentNum;
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
     * Recursive private methode for finding A(S) for a given stack S using dynamic programming.
     *
     * @param permutation The given stack S
     * @return The value of A(S)
     */
    private int getA(IntArray permutation) {
        // Check if the given array (stack) is sorted. If so, return an empty list.
        if (Utils.isSortedPermutation(permutation.getArray())) return 0;

        // Check if the current array has already been computed (e.i., is
        // contained in dp). If so, return the computed value.
        if (this.dp.containsKey(permutation)) return this.dp.get(permutation);

        // Keep track of the best number of operations
        int min = -1;

        // Iterate through all possible indexes for WUE-Operations on the current stack
        for (int i = 0; i < permutation.getLength(); i++) {
            // Apply the improved WUE-Operation on the given array (stack) using the current index
            // and solve the problem recursively.
            IntArray currentPermutation = Utils.improvedWUEOperation(permutation, i);
            int currentNumber = getA(currentPermutation) + 1;

            // If min is still -1 or the current result (currentNumber) is smaller
            // than min, update min to the currentNumber
            if (min == -1 || currentNumber < min) {
                min = currentNumber;
            }
        }

        // Finally, put the smallest number (min) into the dp HashMap for the given permutation and return it.
        this.dp.put(permutation, min);
        return min;
    }
}