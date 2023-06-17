package de.flo.wenigerKrummeTouren.util;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class representing a vector in the n-dimensional space containing the vector's dimension
 * and the vector's components.
 * This class contains useful methods to, e.g. to compote the vector's length or to
 * compute this vector's dot product with another one.
 */
public class Vector {

    /**
     * The point's dimension (>= 0)
     */
    private final int dimension;

    /**
     * The vector's components represented as double-array of length dimension (>= 0)
     */
    private final double[] components;

    /**
     * Constructor taking in the vector's components and if the given array should be cloned
     *
     * @param components The Vectors components
     * @param cloneArray If the given array should be cloned or not
     */
    public Vector(double[] components, boolean cloneArray) {
        this.dimension = components.length;
        this.components = cloneArray ? components.clone() : components;
    }

    /**
     * Constructor only taking the point's components (always cloning the given array)
     *
     * @param components The Vectors components
     */
    public Vector(double[] components) {
        this(components, true);
    }

    /**
     * Public methode to compute the length of this vector to the power of 2.
     * This method might be useful because it's easier to compute than the actual length of the vector.
     *
     * @return The length of this vector to the power of 2
     */
    public double lengthSquared() {
        double length = 0D;

        // Sum up the squares of the single components of the vector
        for (int i = 0; i < this.dimension; i++) {
            length += this.components[i] * this.components[i];
        }

        return length;
    }

    /**
     * Public methode compute the dot product of this vector with another one.
     *
     * @param that The other vector
     * @return The dot product of this vector with the other one
     * @throws IllegalArgumentException If the vector don't have the same dimension
     */
    public double dotProduct(Vector that) {
        // Making sure the vectors have the same dimension
        if (that.dimension != this.dimension) throw new IllegalArgumentException();

        double product = 0D;

        for (int i = 0; i < this.dimension; i++) {
            product += this.components[i] * that.components[i];
        }

        return product;
    }

    /**
     * Public methode compute the cosine of the angle between this vector and another one.
     *
     * @param that The other vector
     * @return The cosine of this vector and the other one
     * @throws IllegalArgumentException If the vectors don't have the same dimension
     */
    public double cos(Vector that) {
        return (this.dotProduct(that)) / (this.length() * that.length());
    }

    /**
     * Public method compute this vector's length using the functionMath#sqrt
     * and the lengthSquared-methode of the class.
     *
     * @return This vector's length
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Implementation of the toString methode returning a string containing the vector's components separated by commas
     *
     * @return A string containing the vector's components separated by commas
     */
    @Override
    public String toString() {
        return Arrays.toString(components).replaceAll("[\\[\\]]", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return dimension == vector.dimension && Arrays.equals(components, vector.components);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dimension);
        result = 31 * result + Arrays.hashCode(components);
        return result;
    }
}
