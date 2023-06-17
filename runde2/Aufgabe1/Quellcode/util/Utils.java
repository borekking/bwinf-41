package de.flo.wenigerKrummeTouren.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Public final utility class containing public static utility functions.
 */
public final class Utils {

    /**
     * Private constructor, such that no instances of this class can be created.
     */
    private Utils() {
    }

    /**
     * Public static function splitting a string at spaces (e.g. words in a sentence).
     *
     * @param str The string
     * @return A string array made up of the substrings that result from splitting the string at spaces. Note that null results in an empty array.
     */
    public static String[] splitBySpace(String str) {
        if (str == null) return new String[0];
        return str.split(" ");
    }

    /**
     * Public static function converting a generic array into a HashSet.
     *
     * @param array The generic array
     * @param <T>   The array's type
     * @return A HashSet containig the array's elements
     */
    public static <T> Set<T> toSet(T[] array) {
        return new HashSet<>(Arrays.asList(array));
    }

    /**
     * Public static function computing the length of a given route given as an array of points using the Point#distance methode
     *
     * @param points The route given as an array of points
     * @return The route's length
     */
    public static double length(Point[] points) {
        double length = 0;

        // Compute the route's length by adding up the length
        // from one point to the next.
        for (int i = 0; i < points.length - 1; i++) {
            length += points[i].distance(points[i + 1]);
        }

        return length;
    }

    /**
     * Public static function computing the length of a given route given as list of points using the Point#distance methode
     *
     * @param list The route given as a list of points
     * @return The route's length
     */
    public static double getLength(List<Point> list) {
        double length = 0D;

        for (int i = 0; i < list.size() - 1; i++) {
            length += list.get(i).distance(list.get(i + 1));
        }

        return length;
    }

    /**
     * Public static function checking weather three points met the turning angle constraint.
     * E.i. the angle between them is at least 90 degrees.
     *
     * @param P The first point
     * @param Q The second point
     * @param R The last point
     * @return If the angle between QP and QR is at least 90 degrees
     */
    public static boolean turningAngleIsValid(Point P, Point Q, Point R) {
        Vector vec1 = Q.vectorToPoint(P), vec2 = Q.vectorToPoint(R);
        return vec1.dotProduct(vec2) <= 0;
    }

    /**
     * Public static function to check if a given route given as an array of points mets the turning angle constraint.
     *
     * @param points The route given as an array of points
     * @return If the turning angle constraint is met by the given route
     */
    public static boolean turningAnglesAreValid(Point[] points) {
        // If the route only has 0, 1 or 2 points, the angle constraint is always met
        // since there are no angles at all
        if (points.length < 3) return true;

        for (int i = 0; i < points.length - 2; i++) {
            if (!turningAngleIsValid(points[i], points[i + 1], points[i + 2]))
                return false;
        }
        return true;
    }

    /**
     * Public static function to check if a given route given as a list of points mets the turning angle constraint.
     *
     * @param points The route given as a list of points
     * @return If the turning angle constraint is met by the given route
     */
    public static boolean turningAnglesAreValid(List<Point> points) {
        for (int i = 0; i < points.size() - 2; i++) {
            Point P = points.get(i);
            Point Q = points.get(i + 1);
            Point R = points.get(i + 2);
            if (!Utils.turningAngleIsValid(P, Q, R)) return false;
        }

        return true;
    }

    /**
     * Private methode used to swap to elements in an array of points given two indexes.
     *
     * @param points The array of points
     * @param i      The first index
     * @param j      The second index
     */
    public static void swap(Point[] points, int i, int j) {
        Point tmp = points[i];
        points[i] = points[j];
        points[j] = tmp;
    }

    /**
     * Public static function for creating a copy of a given list
     *
     * @param list A list
     * @return The copy of the list
     * @param <T> The list's type
     */
    public static <T> List<T> copyList(List<T> list) {
        return new ArrayList<>(list);
    }
}