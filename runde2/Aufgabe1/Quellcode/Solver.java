package de.flo.wenigerKrummeTouren;

import de.flo.wenigerKrummeTouren.util.Point;

/**
 * Abstract parent class for classes solving the given problem
 * of finding a route through given points under certain constraints.
 */
public abstract class Solver {

    /**
     * The amount of points
     */
    private final int size;

    /**
     * The points as an array
     */
    private final Point[] points;

    /**
     * Public constructor taking in the needed points
     * @param points The needed points as an array
     */
    public Solver(Point[] points) {
        this.points = points;
        this.size = points.length;
    }

    /**
     * Abstract method that should return a valid route for the points
     * given in the constructor
     * @return A valid route or null of none was found
     */
    public abstract Point[] solve();

    /**
     * Getter for the points array
     * @return The points array
     */
    public Point[] getPoints() {
        return points;
    }

    /**
     * Getter for a given points
     * @param i The point's index in the points-array
     * @return The point at the given index in the points-array
     */
    public Point getPoint(int i) {
        return this.points[i];
    }

    /**
     * Getter for size, that is the amount of points given
     * @return The amount of points
     */
    public int getSize() {
        return size;
    }
}