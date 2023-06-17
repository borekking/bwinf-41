package de.flo.wenigerKrummeTouren.util;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class representing a point in the n-dimensional space containing the point's dimension
 * and the point's coordinates.
 * This class contains useful methods, e.g. for getting the vector connecting this
 * point to another one or for getting the distance from this point to another one.
 */
public class Point {

    /**
     * The point's dimension (>= 0)
     */
    private final int dimension;

    /**
     * The point's coordinates represented as a double-array of length dimension (>= 0).
     */
    private final double[] coordinates;

    /**
     * Constructor taking in the point's coordinates and if the given array should be cloned.
     *
     * @param coordinates The point's coordinates
     * @param cloneArray If the given array should be cloned or not
     */
    public Point(double[] coordinates, boolean cloneArray) {
        this.dimension = coordinates.length;
        this.coordinates = cloneArray ? coordinates.clone() : coordinates;
    }

    /**
     * Constructor taking in the point's coordinates (always cloning the given array).
     *
     * @param coordinates The point's coordinates
     */
    public Point(double[] coordinates) {
        this(coordinates, true);
    }

    /**
     * Public method returning the connecting vector from this point to another one.
     *
     * @param that The other point
     * @return The connecting vector from this point to another one
     * @throws IllegalArgumentException If both points don't share the same dimension.
     */
    public Vector vectorToPoint(Point that) {
        // Check both points share the same dimension.
        if (that.getDimension() != this.getDimension()) throw new IllegalArgumentException();

        // Create a double-array that will contain the connecting vector's components
        // and fill it up with those components
        double[] components = new double[this.getDimension()];

        for (int i = 0; i < this.dimension; i++) {
            components[i] = that.getCoordinate(i) - this.getCoordinate(i);
        }

        // Return a new vector with the computed components
        return new Vector(components, false);
    }

    /**
     * Public methode computing the distance from the point to another one
     * using the connecting vector.
     *
     * @param that The other point
     * @return The distance from this point to the other point
     * @throws IllegalArgumentException If the points do not share the same dimension
     */
    public double distance(Point that) {
        return this.vectorToPoint(that).length();
    }

    /**
     * Getter for the point's dimension
     *
     * @return The points dimension
     */
    public int getDimension() {
        return this.dimension;
    }

    /**
     * Public method returning a coordinate of this point given its 0-indexed index
     *
     * @param i The coordinate's index (0-indexed)
     * @return The i-th coordinate of this point
     * @throws IllegalArgumentException If the index is not valid, i.e. it's smaller than 0 or greater than
     * or equal to the point's dimension
     */
    public double getCoordinate(int i) {
        if (i < 0 || i >= this.dimension) throw new IllegalArgumentException();
        return this.coordinates[i];
    }

    /**
     * Implementation of the toString methode returning the point's coordinates separated by spaces.
     *
     * @return The point's coordinates separated by spaces.
     */
    @Override
    public String toString() {
        return Arrays.toString(this.coordinates).replaceAll("[\\[\\],]", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return dimension == point.dimension && Arrays.equals(coordinates, point.coordinates);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dimension);
        result = 31 * result + Arrays.hashCode(coordinates);
        return result;
    }
}
