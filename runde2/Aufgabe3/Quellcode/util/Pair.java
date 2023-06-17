package de.flo.pancakeSort.util;

import java.util.Objects;

/**
 * Public record representing a pair of two object of different types.
 *
 * @param <T> The type of the first element
 * @param <E> The type of the second element
 */
public final class Pair<T, E> {
    private final T first;
    private final E second;

    /**
     * @param first The first element of the pair
     * @param second The second element of the pair
     */
    public Pair(T first, E second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    public T first() {
        return first;
    }

    public E second() {
        return second;
    }

    @Override
    public String toString() {
        return "Pair[" +
                "first=" + first + ", " +
                "second=" + second + ']';
    }

}
