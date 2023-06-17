package de.flo.pancakeSort.util;

import java.util.Arrays;
import java.util.List;

/**
 * Public final utility class containing public static utility functions.
 */
public final class Utils {

    /**
     * Private constructor, such that no instances of this class can be created.
     */
    private Utils() {
    }

    /**
     * Public static function to check weather a given array is sorted,
     * e.i. A[1] <= A[2] <= ... <= A[n].
     *
     * @param array The array
     * @return If the array is sorted
     */
    public static boolean isSorted(int[] array) {
        // Arrays of the length 0 or 1 are always sorted
        if (array.length <= 1) return true;

        // Check for all pairs (A[i], A[i+1]) if A[i+1] is greater
        // that A[i]. If so, return false
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) return false;
        }

        // Finally, return true, since the array must be sorted
        return true;
    }

    /**
     * Public static function to check weather a given permutation is sorted,
     * e.i. A[1] = 1, A[2] = 2, ... A[n] = n.
     *
     * @param array The permutation given as an array of integers
     * @return If the array is sorted
     */
    public static boolean isSortedPermutation(int[] array) {
        // Check for each index i if the current element of the
        // array is not i+1. If so, return false
        for (int i = 0; i < array.length; i++) {
            if (array[i] != i + 1) return false;
        }

        // Finally, return true, since the array must be sorted
        return true;
    }

    /**
     * Public static function doing the WUE-Operation on a given array by
     * creating a new one (not changing the given array) given the 0-based index.
     *
     * @param array The array (won't be changes!)
     * @param index The index for the WUE-Operation
     * @return A new array containing the resulting stack when doing the
     * WUE-Operation on the given array
     * @throws IllegalArgumentException If the given index is invalid, e.i. not in the range [0; n-1]
     */
    public static int[] standardWUEOperation(int[] array, int index) {
        // Check if the given index is valid and create the result array
        if (index >= array.length || index < 0) throw new IllegalArgumentException();
        int[] result = new int[array.length - 1];

        // Fill up the result array using the definition of the (normal) WUE-Operations
        for (int i = 0; i < array.length - 1; i++) {
            if (i >= index) {
                result[i] = array[i + 1];
            } else {
                result[i] = array[index - i - 1];
            }
        }

        // Return the result
        return result;
    }


    /**
     * Public static function doing the improved WUE-Operation on a given array by
     * creating a new one (not changing the array) given the 0-based index.
     *
     * @param array The array
     * @param index The index for the WUE-Operation
     * @return The resulting stack when doing improved the WUE-Operation on the given array
     */
    public static int[] improvedWUEOperation(int[] array, int index) {
        // First, get which element of the given array will be removed
        int removed = array[index];

        // Second, compute the normal WUE-Operation on the array (not changing the array)
        int[] result = standardWUEOperation(array, index);

        // Next, make sure the result is actually a permutation,
        // assuming the given array is one.
        for (int i = 0; i < result.length; i++) {
            if (result[i] >= removed) {
                result[i]--;
            }
        }

        // Finally, return the result
        return result;
    }

    /**
     * Public static function doing the WUE-Operation on an array given by the wrapper class
     * Int-Array (not changing the array!) given the needed index and returning an int-array
     * using the wrapper class too.
     *
     * @param array The array given by the wrapper class Int-Array
     * @param index The needed index for the WUE-Operation
     * @return An Int-Array instance containing the resulting stack
     * when doing improved the WUE-Operation on the given array.
     */
    public static IntArray improvedWUEOperation(IntArray array, int index) {
        int[] result = improvedWUEOperation(array.getArray(), index);
        return new IntArray(result);
    }

    /**
     * Private methode used to swap to elements in an int-array given two indexes.
     *
     * @param array The array
     * @param i     The first index
     * @param j     The second index
     */
    public static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    /**
     * Public static function checking weather a given array is
     * a permutation of the numbers from 1 to array.length.
     *
     * @param array The array
     * @return If the given array is a permutation of the numbers from 1 to array.length.
     */
    public static boolean isPermutation(int[] array) {
        // Arrays of length 0 are not a permutation!
        if (array.length == 0) return false;

        // Create a boolean array of size array.length containing
        // only false
        int n = array.length;
        Boolean[] check = new Boolean[n];
        Arrays.fill(check, false);

        // Iterate through the elements of the given array
        for (int element : array) {
            // If the current is not in the range [1; array.length], return false
            if (element < 1 || element > n) return false;

            // Otherwise, set the boolean array if (element-1) to true
            check[element - 1] = true;
        }

        // Return weather all entries of the boolean array are true
        return Arrays.stream(check).allMatch(b -> b);
    }

    /**
     * Return an array consisting of the element of a given list of integers.
     *
     * @param list The list of integers
     * @return The array containing the elements of the list (null if the list is null)
     */
    public static int[] toArray(List<Integer> list) {
        if (list == null) return null;
        return list.stream().mapToInt(i -> i).toArray();
    }
}