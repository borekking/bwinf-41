package de.flo.wenigerKrummeTouren.algorithms.exact;

import de.flo.wenigerKrummeTouren.Solver;
import de.flo.wenigerKrummeTouren.util.Point;
import de.flo.wenigerKrummeTouren.util.Utils;

/**
 * Implementation of the abstract class Solver.
 * This class can solve a given instance of the problem exactly by
 * going through half of all possible permutations iteratively using Heap's
 * Algorithm.
 * To be precise, this class is a concrete implementation of "Algorithmus-5" in the documentation.
 *
 */
public class ExactSolver2 extends Solver {

    /**
     * Public constructor of this class calling the parent's constructor and
     * taking in the points to find a route for.
     *
     * @param points The points the route is created for
     */
    public ExactSolver2(Point[] points) {
        super(points);
    }

    /**
     * Implementation of the solve-methode solving the problem
     * for the given points by iterating through half of all
     * possible permutations of those points
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
        // as well as the permutation's size N
        Point[] points = this.getPoints();
        int N = this.getSize();

        // Iterate through all pairs (a, b) \in \N such that 0 <= a < b <= N-1
        for (int a = 0; a < N; a++) {
            for (int b = a + 1; b < N; b++) {
                // For the current pair create a copy of the original points (route)
                // and set the a-th point at the start and the b-th point at the
                // end of the current permutation (route)
                Point[] currentPoints = points.clone();
                Utils.swap(currentPoints, 0, a);
                Utils.swap(currentPoints, N - 1, b);

                // For the first route (permutation), check if the turning angle constraint is met
                if (Utils.turningAnglesAreValid(currentPoints)) {
                    double length = Utils.length(currentPoints);

                    // If the turning angle constraint is met, check if the
                    // route is shorter than the best route and
                    // update the best Route if so
                    if (bestLength < length) {
                        bestPoints = currentPoints.clone();
                        bestLength = length;
                    }
                }

                // Use Heap's Algorithm to iterate through all possible permutation of the
                // current route (where the start and end points stay as they are)
                int n = 2;
                int[] c = new int[N];
                for (int k = 0; k < N; k++) c[k] = 1;

                while (n < N - 1) {
                    if (c[n] < n) {
                        Utils.swap(currentPoints, n % 2 == 1 ? 1 : c[n], n);
                        c[n]++;
                        n = 2;

                        // For the current permutation of the points (route),
                        // check if the turning angle constraint is met
                        if (Utils.turningAnglesAreValid(currentPoints)) {
                            double length = Utils.length(currentPoints);

                            // If the turning angle constraint is met, check if the
                            // current route is shorter than the best route and
                            // update the best Route if so
                            if (bestLength < length) {
                                bestPoints = currentPoints.clone();
                                bestLength = length;
                            }
                        }
                    } else {
                        c[n] = 1;
                        n += 1;
                    }
                }
            }
        }

        return bestPoints;
    }
}