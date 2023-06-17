package de.flo.wenigerKrummeTouren.algorithms.greedy;

import de.flo.wenigerKrummeTouren.Solver;
import de.flo.wenigerKrummeTouren.util.Point;
import de.flo.wenigerKrummeTouren.util.Utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Implementation of the abstract class Solver.
 * This class uses a greedy approach to solve a given instance
 * of the problem by testing each possible subroute of a given
 * size k to be the start of the route. For each startroute it'll
 * first  check if the turning angle constraint is met and then
 * look for the next points for the route by always using the
 * nearest point meeting the angle constraint.
 * To be precise, this class is an implementation of "Algorithmus-12" in the documentation.
 */
public class GreedySolver4 extends Solver {

    /**
     * The best route found so far as an array of points
     */
    private Point[] bestList = null;

    /**
     * The best route's length initialized with negative one
     */
    private double minLength = -1;

    /**
     * The given size of start subroutes
     */
    private final int startSize;

    /**
     * Public constructor taking in the needed points and the size of
     * the start subroutes tested.
     * Note that this size has to be at least 2.
     *
     * @param points    The needed points as an array
     * @param startSize Size of the start subroutes
     * @throws IllegalArgumentException If the start size is less than 2
     */
    public GreedySolver4(Point[] points, int startSize) {
        super(points);
        if (startSize <= 1 || points.length < startSize) throw new IllegalArgumentException();
        this.startSize = startSize;
    }

    /**
     * Implementation of the solve-methode solving the problem
     * for the given points by testing each possible subroute of a given
     * size k to be the start of the route. For each startroute it'll
     * first  check if the turning angle constraint is met and then
     * look for the next points for the route by always using the
     * nearest point meeting the angle constraint.
     *
     * @return The best route found (might be null if none could be found)
     */
    @Override
    public Point[] solve() {
        // Create a Set containing all points that haven't been used yet.
        // E.i. filling it with all given points. Using a CopyOnWriteArraySet such that
        // one can remove and add element to it while looping through its elements.
        // In fact, that is faster than copying the Set all the time.
        Set<Point> pointsLeft = new CopyOnWriteArraySet<>();
        for (int k = 0; k < this.getSize(); k++) pointsLeft.add(this.getPoint(k));

        // Initialize the current start subroute as an array of points
        // This array will be used for the creation of all starting routes.
        Point[] route = new Point[this.startSize];

        // Start iterating through all possible starting subroutes recursively
        this.checkAllRecursively(route, 0, pointsLeft);

        // Finally, return the best route found so far (might be null)
        return this.bestList;
    }

    /**
     * Private methode for to check each possible start subroute of the
     * given size recursively.
     *
     * @param start The current route
     * @param index The current index the next point will have in the route
     * @param pointsLeft Set containing all points not used yet, e.i. all points not in the route yet
     */
    public void checkAllRecursively(Point[] start, int index, Set<Point> pointsLeft) {
        // If the start subroute has the needed length, check it
        if (index == this.startSize) {
            // First, check weather the current starting route is valid,
            // e.i. the angle constraint is met. If this is not the case, return
            if (!Utils.turningAnglesAreValid(start)) {
                return;
            }

            // Greedily create the rest of the route
            Point[] route = getRoute(start, pointsLeft);

            // Check if route is null (e.i. the route was not found)
            if (route == null) return;

            // Get the current route's length and compare it to the best route
            // If the current one is smaller than the best one, update the best one
            double length = Utils.length(route);

            if (this.minLength == -1 || length < this.minLength) {
                this.minLength = length;
                this.bestList = route.clone();
            }

            return;
        }

        // Otherwise, iterate through all points not in the subroute yet
        for (Point p : pointsLeft) {
            // Add the current point p to the subroute and remove it from the points left
            start[index] = p;
            pointsLeft.remove(p);

            checkAllRecursively(start, index + 1, pointsLeft);

            // Add the current point to the points left again.
            // No need to remove it from the subroute, it'll just be replaces
            pointsLeft.add(p);
        }

    }

    /**
     * Methode for creating the whole route given a starting subroute
     * meeting the angle constraint.
     *
     * @param startRoute the starting subroute
     * @param set The points not used yet
     * @return The complete route created greedily
     */
    private Point[] getRoute(Point[] startRoute, Set<Point> set) {
        // First create the route as an array of points and copy
        // the given start subroute to the start of that array
        Point[] route = new Point[this.getSize()];
        for (int i = 0; i < this.startSize; i++) route[i] = startRoute[i];

        // Create a copy of the points left
        Set<Point> pointsLeft = new HashSet<>(set);

        // Use lastPoint and currentPoint to keep track of
        // the last two points in the current route.
        Point lastPoint = route[this.startSize - 2];
        Point currentPoint = route[this.startSize - 1];

        // Add the rest of the route greedily
        for (int k = this.startSize; k < this.getSize(); k++) {
            // Get the next point using this#getNext
            Point next = getNext(pointsLeft, lastPoint, currentPoint);

            // If next is null (e.i. there is no point left that meets
            // the angle constraint), return null (e.i. no route was found)
            if (next == null) return null;

            // Otherwise, add the found point to the route and remove it from
            // the points left. Also, update the last and current point.
            route[k] = next;
            pointsLeft.remove(next);
            lastPoint = currentPoint;
            currentPoint = next;
        }

        // Finally, update the route found
        return route;
    }

    /**
     * Private methode finding the next point R from a given set of possible
     * points that is the closest to a given point Q and meets the angle
     * constraint for P, Q and R, where P is another point.
     *
     * @param points The set of possible points R
     * @param P      The point P
     * @param Q      The point Q
     * @return The point of the given set of points that is the closest to
     * the point Q and meets the angle constraint for P, Q and R (or null,
     * if there's none)
     */
    private Point getNext(Set<Point> points, Point P, Point Q) {
        // Initialize the closest point (to Q) and its length with null and -1
        Point bestPoint = null;
        double bestDistance = -1;

        // Iterate through all possible points
        for (Point point : points) {
            // For the current point P, get its distance to Q
            // and if the angle constraint is met for P, Q and R
            double curDistance = Q.distance(point);
            boolean valid = Utils.turningAngleIsValid(P, Q, point);

            // Update the best point and its length if the angle constraint is met (valid)
            // and the best point still null or the current distance from P to Q is smaller
            // than from the bestPoint to Q.
            if (valid && (bestDistance == -1 || curDistance < bestDistance)) {
                bestPoint = point;
                bestDistance = curDistance;
            }
        }

        // Return the best point found (might be null)
        return bestPoint;
    }
}