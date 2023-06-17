package de.flo.pancakeSort.util;

import java.util.Arrays;

/**
 * This class is an Int-Array wrapper, providing some useful methods.
 * E.g., checking if the array is sorted, swapping elements and
 * especially computing the array's hashcode.
 *
 */
public class IntArray implements Cloneable {

    /**
     * The internal int array
     */
    private final int[] array;

    /**
     * Public constructor taking an int array and set it to be this classes
     * internal int-array, not(!) copying the given array.
     *
     * @param array The array that will be used as this classes internal array, not being copied!
     */
    public IntArray(int[] array) {
        this.array = array;
    }

    /**
     * Public constructor only taking the array's size and creating
     * a new one (hence filled with zeros).
     *
     * @param size The array's size
     */
    public IntArray(int size) {
        this(new int[size]);
    }

    /**
     * Public methode for checking weather the int array represented by this class
     * is sorted (e.i., A[1] <= ... <= A[n]), using the Utility function.
     *
     * @return If this array is sorted.
     */
    public boolean isSorted() {
        return Utils.isSorted(this.array);
    }

    /**
     * Public methode swapping two elements of the class using the Utility function.
     *
     * @param i The first index
     * @param j The second index
     */
    public void swap(int i, int j) {
        Utils.swap(this.array, i, j);
    }

    @Override
    public Object clone() {
        return new IntArray(this.array.clone());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntArray intArray = (IntArray) o;
        return Arrays.equals(array, intArray.array);
    }

    /**
     * Implementation of the hashCode-methode using the default hashing
     * for int-arrays.
     *
     * @return The default hash-code for integer arrays
     */
    @Override
    public int hashCode() {
        if (this.array == null) return 0;

        int result = 1;
        for (int element : this.array) {
            result = 31 * result + element;
        }

        return result;
    }

    /**
     * Implementation of the toString-methode using Arrays#toString
     *
     * @return The Arrays#toString-string of the internal array
     */
    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    /**
     * Public getter for an element of this array given its array (0-indexed)
     *
     * @param index The zero-based index
     * @return The element at the given index
     */
    public int get(int index) {
        return array[index];
    }

    /**
     * Public setter for elements in this array given an elements index
     * and a new value.
     *
     * @param index The elements index
     * @param value The elements new value
     */
    public void set(int index, int value) {
        this.array[index] = value;
    }

    /**
     * Public getter for this array's length.
     *
     * @return The length of the internal array
     */
    public int getLength() {
        return this.array.length;
    }

    /**
     * Public getter for the internal array, not copying it!
     *
     * @return The internal in array.
     */
    public int[] getArray() {
        return array;
    }
}