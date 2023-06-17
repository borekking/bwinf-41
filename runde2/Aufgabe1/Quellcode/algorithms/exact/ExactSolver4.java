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
 * going through all possible permutations recursively and skipping
 * necessary branches.
 * To be precise, this class is an implementation of "Algorithmus-6" in the documentation.
 */
public class ExactSolver4 extends Solver {

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
    public ExactSolver4(Point[] points) {
        super(points);
    }

    /**
     * Implementation of the solve-methode solving the problem
     * for the given points by iterating through all the
     * possible permutations of those points recursively
     * skipping necessary branches.
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
        checkAllRecursively(pointsLeft, currentList, 0D);

        // Return null if the best list is null, or return the best list as an array.
        if (this.bestList == null) return null;
        return this.bestList.toArray(new Point[0]);
    }

    /**
     * Private methode that is used to recursively check all possible
     * permutations of all points skipping necessary branches.
     *
     * @param pointsLeft The points which are currently not in the route
     * @param currentList The current route as a list of points
     * @param currentLength The current route's length
     */
    private void checkAllRecursively(Set<Point> pointsLeft, List<Point> currentList, double currentLength) {
        // Skip the current branch if the current route's length is bigger
        // than the best route's length (if that is not null)
        if (this.minLength != -1 && this.minLength < currentLength) {
            return;
        }

        // If there are no points left, update the best list to the
        // current one by copying.
        // Note that the current route is smaller than the best one
        // because of the last if-statement
        if (pointsLeft.isEmpty()) {
            this.minLength = currentLength;
            this.bestList = Utils.copyList(currentList);
            return;
        }

        // Get the current route's size (#points)
        int size = currentList.size();

        // Iterate through all unused points (e.i.,
        // points that are not in the route yet)
        for (Point nextPoint : pointsLeft) {
            // If there is more than one point in the current list (route),
            // make sure if turning angle constraint is met by the last two
            // points in the route and nextPoint before recursively going on
            if (size >= 2) {
                Point P = currentList.get(size - 2);
                Point Q = currentList.get(size - 1);

                if (!Utils.turningAngleIsValid(P, Q, nextPoint)) continue;
            }

            // Compute the distance from the last point of the current list to nextPoint.
            // The distance will be 0 if the current list is empty.
            double dist = currentList.isEmpty() ? 0D : currentList.get(size - 1).distance(nextPoint);

            // Set the current point (nextPoint) at the end of the current route and remove it from the points left
            pointsLeft.remove(nextPoint);
            currentList.add(nextPoint);

            // Recursively check all routes starting with the current subroute
            checkAllRecursively(pointsLeft, currentList, currentLength + dist);

            // Remove the current point from the current route and add it to the points left again
            currentList.remove(nextPoint);
            pointsLeft.add(nextPoint);
        }
    }
}