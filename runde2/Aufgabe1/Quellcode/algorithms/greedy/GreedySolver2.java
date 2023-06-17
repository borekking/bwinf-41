package de.flo.wenigerKrummeTouren.algorithms.greedy;

import de.flo.wenigerKrummeTouren.Solver;
import de.flo.wenigerKrummeTouren.util.Point;
import de.flo.wenigerKrummeTouren.util.Utils;

import java.util.Set;

/**
 * Implementation of the abstract class Solver.
 * This class uses a greedy approach to solve a given instance
 * of the problem by testing each possible point to be the route's
 * starting point. And for each one, add the left points greedily to the
 * route by always using the nearest point meeting the angle constraint.
 * To be precise, this class is an implementation of "Algorithmus-10" in the documentation.
 */
public class GreedySolver2 extends Solver {

    /**
     * Public constructor of this class calling the parent's constructor and
     * taking in the points to find a route for.
     *
     * @param points The points the route is created for
     */
    public GreedySolver2(Point[] points) {
        super(points);
    }

    /**
     * Implementation of the solve-methode solving the problem
     * for the given points by testing each possible point to be the route's
     * starting point. And for each one, add the left points greedily to the
     * route by always using the nearest point meeting the angle constraint.
     *
     * @return The best route found (might be null if none could be found)
     */
    @Override
    public Point[] solve() {
        // If the amount of points given is smaller than 3,
        // the result is only the given points (since there won't
        // be any angles)
        if (this.getSize() <= 2) {
            return this.getPoints().clone();
        }

        // Initializing the best route and its length found so far
        // with null and -1.
        Point[] bestRoute = null;
        double bestLength = -1;

        // Choose each possible point to be the starting point of
        // the route using its index. Hence, iterating through all indexes.
        for (int i = 0; i < this.getSize(); i++) {
            // Greedily get the current route with the starting point
            // given by the current index i.
            Point[] currentRoute = solve(i);

            // If currentRoute is null (e.i., no route was found),
            // continue with the next index.
            if (currentRoute == null) continue;

            // Get the current route's length and compare it to
            // the best one. If the current length is smaller,
            // update the best route.
            double curLength = Utils.length(currentRoute);

            if (bestLength == -1 || curLength < bestLength) {
                bestRoute = currentRoute;
                bestLength = curLength;
            }
        }

        // Finally, return the best route
        return bestRoute;
    }

    /**
     * Private methode for greedily finding a route given the index
     * of a starting point
     *
     * @param i The index of the starting point
     * @return The route or null if none was found
     */
    private Point[] solve(int i) {
        // Initialize the route (points) as an array of points
        // and a Set containing all given points (will be the
        // set containing all points not used yet)
        Point[] points = new Point[this.getSize()];
        Set<Point> pointsLeft = Utils.toSet(this.getPoints());

        // Choose the first point of the route, add it to
        // the route and remove it from the points left
        Point firstPoint = this.getPoint(i);
        points[0] = firstPoint;
        pointsLeft.remove(firstPoint);

        // Get the second point to be the point that is the
        // closest to the first point
        Point secondPoint = getNext(pointsLeft, firstPoint);
        points[1] = secondPoint;
        pointsLeft.remove(secondPoint);

        // Use lastPoint and currentPoint to keep track of
        // the last two points in the current route.
        Point lastPoint = firstPoint;
        Point currentPoint = secondPoint;

        // Now, get the next elements for the route
        for (int k = 2; k < this.getSize(); k++) {
            // Get the next point using this#getNext
            Point next = getNext(pointsLeft, lastPoint, currentPoint);

            // If next is null (e.i. there is no point left that meets
            // the angle constraint), return null (e.i. no route was found)
            if (next == null) return null;

            // Otherwise, add the found point to the route and remove it from
            // the points left. Also, update the last and current point.
            points[k] = next;
            pointsLeft.remove(next);
            lastPoint = currentPoint;
            currentPoint = next;
        }

        // Finally, return the route
        return points;
    }

    /**
     * Private method finding a point P that is the closest to a given point Q
     * of the points in a given set.
     *
     * @param points The set of points
     * @param Q The point Q
     * @return The point of the given set that is the closest to Q
     */
    private Point getNext(Set<Point> points, Point Q) {
        // Initialize the closest point (to Q) and its length with null and -1
        Point bestPoint = null;
        double bestDistance = -1;

        // Iterate through all possible points
        for (Point point : points) {
            // For the current point P get the distance from
            // P to the given point Q
            double curDistance = Q.distance(point);

            // If the current distance is smaller than the best distance
            // or the best distance is still -1, update the best point and
            // the best distance
            if (bestDistance == -1 || curDistance < bestDistance) {
                bestPoint = point;
                bestDistance = curDistance;
            }
        }

        return bestPoint;
    }

    /**
     * Private methode finding the next point R from a given set of possible
     * points that is the closest to a given point Q and meets the angle
     * constraint for P, Q and R, where P is another point.
     *
     * @param points The set of possible points R
     * @param P The point P
     * @param Q The point Q
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