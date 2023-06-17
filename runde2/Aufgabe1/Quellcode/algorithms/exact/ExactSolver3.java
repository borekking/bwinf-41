package de.flo.wenigerKrummeTouren.algorithms.exact;

import de.flo.wenigerKrummeTouren.Solver;
import de.flo.wenigerKrummeTouren.util.Point;
import de.flo.wenigerKrummeTouren.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Implementation of the abstract class Solver.
 * This class can solve a given instance of the problem exactly by
 * going through all possible permutations recursively.
 * To be precise, this class is an implementation of "Algorithmus-4" in the documentation.
 */
public class ExactSolver3 extends Solver {

    /**
     * The best route found so far as a list of points
     */
    private List<Point> bestList = null;

    /**
     * The best route's length initialized with negative one
     */
    private double minLength = -1;

    /**
     * Public constructor of this class calling the parent's constructor and
     * taking in the points to find a route for.
     *
     * @param points The points the route is created for
     */
    public ExactSolver3(Point[] points) {
        super(points);
    }

    /**
     * Implementation of the solve-methode solving the problem
     * for the given points by iterating through all the
     * possible permutations of those points recursively.
     *
     * @return The best possible route (might be null)
     */
    @Override
    public Point[] solve() {
        // Create a Set containing all points that haven't been used yet.
        // E.i. filling it with all given points. Using a CopyOnWriteArraySet such that
        // one can remove and add element to it while looping through its elements.
        // In fact, that is faster than copying the Set all the time.
        Set<Point> pointsLeft = new CopyOnWriteArraySet<>();
        for (int k = 0; k < this.getSize(); k++) pointsLeft.add(this.getPoint(k));

        // Initialize the current list (route) as an empty list.
        List<Point> currentList = new ArrayList<>();

        // Call the recursive checking of all possible routes.
        checkAllRecursively(pointsLeft, currentList);

        // Return null if the best list is null, or return the best list as an array.
        if (this.bestList == null) return null;
        return this.bestList.toArray(new Point[0]);
    }

    /**
     * Private methode that is used to recursively check all possible
     * permutations of all points.
     *
     * @param pointsLeft The points which are currently not in the route
     * @param currentList The current route as a list of points
     */
    private void checkAllRecursively(Set<Point> pointsLeft, List<Point> currentList) {
        // If there are no points left, check the current route.
        // E.i., check if it mets the angle constraint and compare it to the best
        // route so far.
        if (pointsLeft.isEmpty()) {
            check(currentList);
            return;
        }

        // Iterate through all unused points (e.i.,
        // points that are not in the route yet)
        for (Point point : pointsLeft) {
            // Set the current point at the end of the current route and remove it from the points left
            pointsLeft.remove(point);
            currentList.add(point);

            // Recursively check all routes starting with the current subroute
            checkAllRecursively(pointsLeft, currentList);

            // Remove the current point from the current route and add it to the points left again
            currentList.remove(point);
            pointsLeft.add(point);
        }
    }

    /**
     * Private methode called to test a given permutation of the points.
     * Checking if the given route mets the turning angle constraint and updating
     * the currently best list if the given list (route) is shorter than
     * the best one
     *
     * @param list The current list of points (route)
     */
    private void check(List<Point> list) {
        if (!Utils.turningAnglesAreValid(list)) return;

        double length = Utils.getLength(list);

        if (this.minLength == -1 || length < this.minLength) {
            this.minLength = length;
            this.bestList = Utils.copyList(list);
        }
    }
}