package de.flo.pancakeSort.algorithms.normalWUEOperation;

import de.flo.pancakeSort.PZSolver;
import de.flo.pancakeSort.util.Pair;
import de.flo.pancakeSort.util.Utils;

/**
 * Implementation of the abstract class PZSolver that can find
 * P(n) for a given natural number n and a corresponding example stack
 * of the given size that needs at least P(n) WUE-Operations to be
 * converted into a sorted stack.
 * This algorithm works by computing A(S) for each possible permutation of the
 * given size n using Heap's Algorithm and is a concrete implementation of "Algorithmus-10" in the documentation
 * using "Algorithmus-8" for getting A(S).
 */
public class PZSolver2 extends PZSolver {

    /**
     * Public constructor only taking in the value of n, such that P(n) can be computed.
     *
     * @param n The value n, such that P(n) can be computed.
     */
    public PZSolver2(int n) {
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
            list[i-1] = i;
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
        // Check if the permutation is sorted
        if (Utils.isSorted(permutation)) return 0;

        // Keep track of the smallest value for A(S) found recursively
        int min = -1;

        // Iterate through all possible indexes for WUE-Operations on the current stack
        for (int i = 0; i < permutation.length; i++) {
            // Apply the normal WUE-Operation on the given array (stack) using the current index
            // and solve the problem recursively.
            int[] currentPermutation = Utils.standardWUEOperation(permutation, i);
            int currentNumber = getA(currentPermutation) + 1;

            // If the smallest A(S) value min is still -1 or the current one is smaller
            // than min, update min to be the current value currentNum.
            if (min == -1 || currentNumber < min) {
                min = currentNumber;
            }
        }

        // Return the value of A(S)
        return min;
    }
}