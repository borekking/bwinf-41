package de.flo.wenigerKrummeTouren.algorithms.exact;

import de.flo.wenigerKrummeTouren.Solver;
import de.flo.wenigerKrummeTouren.util.Point;
import de.flo.wenigerKrummeTouren.util.Utils;

/**
 * Implementation of the abstract class Solver.
 * This class can solve a given instance of the problem exactly by
 * going through all possible permutations iteratively using Heap's
 * Algorithm.
 * To be precise, this class implements "Algorithmus-3" in the documentation.
 *
 */
public class ExactSolver1 extends Solver {

    /**
     * Public constructor of this class calling the parent's constructor and
     * taking in the points to find a route for.
     *
     * @param points The points the route is created for
     */
    public ExactSolver1(Point[] points) {
        super(points);
    }

    /**
     * Implementation of the solve-methode solving the problem
     * for the given points by iterating through all possible permutations
     * of those points
     *
     * @return The best possible route (might be null)
     */
    @Override
    public Point[] solve() {
        // Initialize the best points (route), and it's length with null and -1.
        // Those variables will keep track of the best route found so far.
        Point[] bestPoints = null;
        double bestLength = -1;

        // Initialize the first permutation using given points from this#getPoints,
        // as well as the permutation's size N and the array c needed for Heap's Algorithm
        Point[] points = this.getPoints();
        int N = this.getSize();
        int[] c = new int[N];

        // For the first route (permutation), check if the turning angle constraint is met
        if (Utils.turningAnglesAreValid(points)) {
            bestPoints = points.clone();
            bestLength = Utils.length(bestPoints);
        }

        // Use the Heap-Algorithm to iterate through all
        // permutations of the points
        int n = 1;
        while (n < N) {
            if (c[n] < n) {
                Utils.swap(points, n % 2 == 0 ? 0 : c[n], n);
                c[n]++;
                n = 1;

                // For the current permutation of the points (route),
                // check if the turning angle constraint is met
                if (Utils.turningAnglesAreValid(points)) {
                    double length = Utils.length(points);

                    // If the turning angle constraint is met, check if the
                    // current route is shorter than the best route and
                    // update the best Route if so
                    if (bestLength < length) {
                        bestPoints = points.clone();
                        bestLength = length;
                    }
                }
            } else {
                c[n] = 0;
                n += 1;
            }
        }

        return bestPoints;
    }
}