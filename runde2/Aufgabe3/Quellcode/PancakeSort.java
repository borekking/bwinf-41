package de.flo.pancakeSort;

/**
 * Abstract parent class for classes solving the MWO-problem of find A(S) and a
 * corresponding result for a given stack S.
 *
 */
public abstract class PancakeSort {

    /**
     * The given stack needed for solving the problem represented by an int array
     */
    private final int[] array;

    /**
     * Public constructor taking in the needed stack.
     *
     * @param array The stack given as an int array
     */
    public PancakeSort(int[] array) {
        this.array = array;
    }

    /**
     * Abstract methode that should return a valid WUE-Operation series converting
     * the given stack into a sorted one, of the minimum size A(S)
     *
     * @return A series of WUE-Operations
     */
    public abstract int[] solve();

    /**
     * Getter for the int array representing the stack
     * @return The int array representing the stack
     */
    public int[] getArray() {
        return array;
    }
}