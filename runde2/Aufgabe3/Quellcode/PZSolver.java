package de.flo.pancakeSort;

import de.flo.pancakeSort.util.Pair;

/**
 * Abstract parent class of classes solving the PWZ-Problem of finding P(n).
 *
 */
public abstract class PZSolver {

    /**
     * The value of n, as a protected variable such that subclasses can use it.
     */
    protected final int n;

    /**
     * Public constructor only taking in the value of n, such that P(n) can be computed.
     *
     * @param n The value n, such that P(n) can be computed.
     */
    public PZSolver(int n) {
        this.n = n;
    }

    /**
     * Abstract methode that should return a P(N) and a corresponding example, e.i.,
     * a stack of the size n that needs at least P(N) WUE-Operation to be sorted.
     *
     * @return P(n) and the corresponding example stack
     */
    public abstract Pair<Integer, int[]> solve();
}