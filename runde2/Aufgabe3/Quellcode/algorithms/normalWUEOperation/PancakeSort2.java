package de.flo.pancakeSort.algorithms.normalWUEOperation;

import de.flo.pancakeSort.PancakeSort;
import de.flo.pancakeSort.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the abstract class PancakeSort that can find
 * one smallest series of WUE-Operations to convert the given stack
 * into a sorted one by recursively solving the subproblems of the
 * problem after applying the normal WUE-Operation for all possible
 * indexes.
 * This algorithmen is a concrete implementation of "Algorithmus-6" in the documentation.
 */
public class PancakeSort2 extends PancakeSort {

    /**
     * Public constructor of this class calling the parent's constructor and taking in
     * a stack as an int array.
     *
     * @param array The stack represented by an int array
     */
    public PancakeSort2(int[] array) {
        super(array);
    }

    /**
     * Implementation of the solve-methode solving the MWO-Problem as described above.
     *
     * @return An int array containing the indexes (0-based) for the WUE-Operations
     */
    @Override
    public int[] solve() {
        return Utils.toArray(solve(this.getArray()));
    }

    /**
     * Recursive private methode for finding the smallest list of indexes converting the
     * given stack (array) into a sorted one
     *
     * @param array The current stack
     * @return The smallest possible list of 0-based indexes that can convert the given
     * stack in a sorted one using WUE-Operations.
     */
    private List<Integer> solve(int[] array) {
        // Check if the given array (stack) is sorted. If so return an empty list.
        if (Utils.isSorted(array)) return new ArrayList<>();

        // Keep track of the best list (of indexes for the WUE-Operations) using this list
        List<Integer> bestList = null;

        // Iterate through all possible indexes for WUE-Operations on the current stack
        for (int index = 0; index < array.length; index++) {
            // Apply the normal WUE-Operation on the given array (stack) using the current index
            // and solve the problem recursively.
            int[] currentArray = Utils.standardWUEOperation(array, index);
            List<Integer> recursiveResult = solve(currentArray);

            // If the best list is still null or the current list (recursiveResult.size() + 1!)
            // si smaller than the best list, update the best list to the current one
            if (bestList == null || recursiveResult.size() + 1 < bestList.size()) {
                // Create the current list by first adding the current index and then adding the recursive result.
                bestList = new ArrayList<>();
                bestList.add(index);
                bestList.addAll(recursiveResult);
            }
        }

        // Finally, return the best list (of indexes)
        return bestList;
    }
}