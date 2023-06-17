package de.flo.JA1_reimerei;

import java.util.Objects;

/**
 * Class representing a pair / tuple (ordered set) of size 2
 *
 * @param <T> The first element's type
 * @param <E> The second element's type
 */
public class Pair<T, E> {

    /**
     * The first element of the pair
     */
    private final T first;

    /**
     * The second element of the pair
     */
    private final E second;

    /**
     * A private constructor taking in the two elements of the pair (private since a factory-method is used)
     *
     * @param first  The first element of the pair
     * @param second The second element of the pair
     */
    private Pair(T first, E second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Factory-Method creating a new pair, given its elements
     *
     * @param t   The first element of the pair
     * @param e   The second element of the pair
     * @param <T> The first element's type
     * @param <E> The second element's type
     * @return A new pair containing the given elements in the given order
     */
    public static <T, E> Pair<T, E> of(T t, E e) {
        return new Pair<>(t, e);
    }

    /**
     * Getter for the first element
     *
     * @return The first element
     */
    public T getFirst() {
        return first;
    }

    /**
     * Getter for the second element
     *
     * @return The second element
     */
    public E getSecond() {
        return second;
    }

    /**
     * Equals-methode comparing this object to another one. The other object is equal to this one if the object's class is correct and the first and second element are equal.
     *
     * @param that The other object
     * @return If the objects are equal
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) that;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    /**
     * Methode creating the pair's hash code based on the first and second element
     *
     * @return The hash code based on the elements
     */
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    /**
     * A to-string methode representing the pair using the format (first, second)
     *
     * @return This pair in string representation
     */
    @Override
    public String toString() {
        return String.format("(%s, %s)", this.first, this.second);
    }
}
