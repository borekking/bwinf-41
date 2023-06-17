package de.flo.pancakeSort.algorithms.dynamicProgramming;

import de.flo.pancakeSort.PancakeSort;
import de.flo.pancakeSort.util.IntArray;
import de.flo.pancakeSort.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the abstract class PancakeSort that can find
 * one smallest series of WUE-Operations to convert the given stack
 * into a sorted one by recursively solving the subproblems of the
 * problem after applying the improved(!) WUE-Operation for all possible
 * indexes using dynamic programming to not(!) compute any subproblem twice.
 *
 * This algorithmen is a concrete implementation of "Algorithmus-17" in the documentation.
 */
public class PancakeSortDP extends PancakeSort {

    /**
     * HashMap mapping from the int-array wrapper class IntArray
     * to lists of integer. This hashmap contains the results of the
     * solveRecursively-methode, such that subproblems that have already been solved are
     * returned instantly.
     */
    private final Map<IntArray, List<Integer>> dp;

    /**
     * Public constructor of this class calling the parent's constructor and taking in
     * a stack as an int array.
     *
     * @param array The stack represented by an int array
     */
    public PancakeSortDP(int[] array) {
        super(array);

        // Initialize the Map used for dynamic programming with an empty HashMap
        this.dp = new HashMap<>();
    }

    /**
     * Implementation of the solve-methode solving the MWO-Problem as described above.
     *
     * @return An int array containing the indexes (0-based) for the WUE-Operations
     */
    @Override
    public int[] solve() {
        // Convert the given int array into an instance of the wrapper class IntArray
        IntArray arr = new IntArray(this.getArray());
        return Utils.toArray(solveRecursively(arr));
    }

    /**
     * Recursive private methode for finding the smallest list of indexes converting the
     * given stack (array) into a sorted one using dynamic programming.
     *
     * @param array The current stack
     * @return The smallest possible list of 0-based indexes that can convert the given
     * stack in a sorted one using WUE-Operations.
     */
    private List<Integer> solveRecursively(IntArray array) {
        // Check if the given array (stack) is sorted. If so, return an empty list.
        if (Utils.isSortedPermutation(array.getArray())) return new ArrayList<>();

        // Check if the current array has already been computed (e.i., is
        // contained in dp). If so, return the computed value.
        if (this.dp.containsKey(array)) return this.dp.get(array);

        // Keep track of the best list (of indexes for the WUE-Operations) using this list
        List<Integer> bestList = null;

        // Iterate through all possible indexes for WUE-Operations on the current stack
        for (int i = 0; i < array.getLength(); i++) {
            // Apply the improved WUE-Operation on the given array (stack) using the current index
            // and solve the problem recursively.
            IntArray currentArray = Utils.improvedWUEOperation(array, i);
            List<Integer> recursiveResult = solveRecursively(currentArray);

            // If the best list is still null or the current list (recursiveResult.size() + 1!)
            // is smaller than the best list, update the best list to the current one
            if (bestList == null || recursiveResult.size() + 1 < bestList.size()) {
                // Create the current list by first adding the current index and then adding the recursive result.
                bestList = new ArrayList<>();
                bestList.add(i);
                bestList.addAll(recursiveResult);
            }
        }

        // Finally, put the best list (of indexes) into the dp HashMap for the given array and return it.
        this.dp.put(array, bestList);
        return bestList;
    }
}