package de.flo.pancakeSort.algorithms.betterWUEOperation;

import de.flo.pancakeSort.PancakeSort;
import de.flo.pancakeSort.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the abstract class PancakeSort that can find
 * one smallest series of WUE-Operations to convert the given stack
 * into a sorted one by going through all sizes of index-series (0 to n-1)
 * and testing each possible list of indexes util finding one that converts
 * the given stack into a sorted one by applying the corresponding
 * WUE-Operations to the indexes. Now, using the better WUE-Operations.
 * This algorithmen is a concrete implementation of "Algorithmus-13" in the documentation.
 */
public class PancakeSort3 extends PancakeSort {

    /**
     * Field for the best list of indexes. Set to a concrete value as soon as
     * a list of indexes is found that converts the given stack into a sorted one.
     */
    private List<Integer> bestList = null;

    /**
     * Public constructor taking in the needed stack.
     *
     * @param array The stack given as an int array
     */
    public PancakeSort3(int[] array) {
        super(array);
    }

    /**
     * Implementation of the solve-methode solving the MWO-Problem as described above.
     *
     * @return An int array containing the indexes (0-based) for the WUE-Operations
     */
    @Override
    public int[] solve() {
        // The size of the given stack (array)
        int size = this.getArray().length;

        // Go through all possible sizes of index series.
        // Note that the size (size = n-1) always creates a sorted stack.
        for (int m = 0; m < size; m++) {
            // Initialize the current list of indexes
            List<Integer> list = new ArrayList<>();

            // Recursively test out every possible list of indexes of the current size m.
            // If the methode returns true, a list of the current size m was found (bestList).
            if (solveRecursively(list, m, size)) {
                break;
            }
        }

        // Return the best list (of indexes) as an int array
        return Utils.toArray(this.bestList);
    }

    /**
     * Private methode for recursively trying out every possible list of indexes
     * of a given size m used for the WUE-Operation and return weather any list of
     * this size can sort the given stack (permutation) when used as a series of WUE-Operations.
     * Now, using the improved WUE-Operations and a slightly easier check.
     *
     * @param list The current list of indexes
     * @param m    The wanted size for the list of indexes
     * @param n    The size of the given stack
     * @return true, if a list of indexes of the given size m, converting
     * the given stack into a sorted one, exists. false otherwise.
     */
    private boolean solveRecursively(List<Integer> list, int m, int n) {
        // If the current list of indexes has reached the wanted size,
        // check if converts the given stack into a sorted one by
        // applying the normal WUE-Operation.
        if (list.size() == m) {
            // Copy the given stack (array)
            int[] result = this.getArray().clone();

            // Apply the WUE-Operations given by the current list of indexes
            for (int index : list) {
                result = Utils.improvedWUEOperation(result, index);
            }

            // Check if the resulting stack is sorted.
            // If so, set the bestList field to the current list and return true.
            if (Utils.isSortedPermutation(result)) {
                this.bestList = new ArrayList<>(list);
                return true;
            }

            // Otherwise, return false.
            return false;
        }

        // Iterate through all possible indexes (0 to n - list.size() - 1)
        for (int index = 0; index < n - list.size(); index++) {
            // Add the current index to the list of indexes
            list.add((Integer) index);

            // Recursively use this methode to create lists of indexes
            // starting with the current one (or check the current one)
            if (solveRecursively(list, m, n)) {
                return true;
            }

            // Remove the last element of the list
            list.remove(list.size() - 1);
        }

        return false;
    }
}