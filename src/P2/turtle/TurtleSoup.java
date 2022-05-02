/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import turtle.Vector.Vector2;

import java.util.*;

public class TurtleSoup {

    /**
     * Draw a square.
     *
     * @param turtle     the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        drawRegularPolygon(turtle, 4, sideLength);
    }

    /**
     * Determine inside angles of a regular polygon.
     * <p>
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     *
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        return 180 - 360.0 / sides;
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * <p>
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     *
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        return (int) Math.round(360.0 / (180.0 - angle));
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * <p>
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     *
     * @param turtle     the turtle context
     * @param sides      number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        double turnAngle = 180 - calculateRegularPolygonAngle(sides);
        turtle.turn(-90);
        for (int i = 1; i <= sides; i++) {
            turtle.forward(sideLength);
            turtle.turn(turnAngle);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * <p>
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360.
     * <p>
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     *
     * @param currentBearing current direction as clockwise from north
     * @param currentX       current location x-coordinate
     * @param currentY       current location y-coordinate
     * @param targetX        target point x-coordinate
     * @param targetY        target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     * must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY, int targetX, int targetY) {
        Vector2 defaultDir = new Vector2(0, 1);
        Vector2 targetDir = (new Vector2(targetX - currentX, targetY - currentY)).normalize();
        double angleDiff = (Math.acos(Vector2.dot(defaultDir, targetDir))) * 180 / Math.PI;
        if (Vector2.cross(targetDir, defaultDir).z() < 0) angleDiff = -angleDiff;
        double bearingAdjustment = angleDiff - currentBearing;
        while (bearingAdjustment < 0) bearingAdjustment += 360;
        return bearingAdjustment;
    }

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * <p>
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     *
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     * otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {

        Vector2 currentDir = new Vector2(0, 1);
        List<Double> res = new ArrayList<>();
        for (int i = 0; i < xCoords.size() - 1; i++) {
            Vector2 nextDir = (new Vector2(xCoords.get(i + 1) - xCoords.get(i), yCoords.get(i + 1) - yCoords.get(i))).normalize();
            double angleDiff = Math.acos(Vector2.dot(currentDir, nextDir)) * 180.0 / Math.PI;
            if (Vector2.cross(nextDir, currentDir).z() < 0) angleDiff = -angleDiff;
            while (angleDiff < 0) angleDiff += 360;
            res.add(angleDiff);
            currentDir = nextDir;
        }

        return res;
    }

    /**
     * Given a set of points, compute the convex hull, the smallest convex set that contains all the points
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and
     * there are other algorithms too.
     *
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points) {
        if (points.size() <= 2) return new HashSet<>(points);
        List<Point> pointsList = new ArrayList<>(points);
        Set<Point> res = new HashSet<>();

        int originalPointIndex = 0;
        for (int i = 1; i < pointsList.size(); i++) {
            if (pointsList.get(i).y() < pointsList.get(originalPointIndex).y()) {
                originalPointIndex = i;
            }
        }

        int firstPointIndex = -1;
        int secondPointIndex = originalPointIndex;
        int thirdPointIndex = -1;
        Vector2 indentDir = new Vector2(-1, 0);
        double maxAngleDiff = 0;
        double maxDirNorm = 0;

        do {
            maxAngleDiff = 0;
            for (int i = 0; i < pointsList.size(); i++) {
                if (i == firstPointIndex || i == secondPointIndex) continue;
                double currentAngleDiff =
                        Vector2.getAngleDiff(new Vector2(pointsList.get(secondPointIndex), pointsList.get(i)), indentDir);
                double currentDirNorm = (new Vector2(pointsList.get(secondPointIndex), pointsList.get(i))).norm();
                if (currentAngleDiff > maxAngleDiff) {
                    maxAngleDiff = currentAngleDiff;
                    thirdPointIndex = i;
                    maxDirNorm = currentDirNorm;
                }

                if (Math.abs(currentAngleDiff - maxAngleDiff) < 1e-3 && (currentDirNorm > maxDirNorm)) {
                    maxAngleDiff = currentAngleDiff;
                    thirdPointIndex = i;
                    maxDirNorm = currentDirNorm;
                }

            }
            res.add(pointsList.get(secondPointIndex));
            firstPointIndex = secondPointIndex;
            secondPointIndex = thirdPointIndex;
            indentDir = new Vector2(pointsList.get(secondPointIndex), pointsList.get(firstPointIndex));
        } while (thirdPointIndex != originalPointIndex);

        return res;
    }

    /**
     * Draw your personal, custom art.
     * <p>
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     *
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {

//        drawRegularPolygon(turtle, 6, 80);
//        drawHelix(turtle, 3, 2.2, 512, 1);
        drawHelix(turtle, 3, 2.2, 384, 2);
//        drawHelix(turtle, 5, 0.5, 256, 1);
//        drawHelix(turtle, 4, 0.8, 256, 2);
    }

    public static void drawHelix(Turtle turtle, int sides, double offset, int iterations, int step) {
        double angle = (180 - calculateRegularPolygonAngle(sides)) + offset;
        for (int i = 1; i <= iterations; i += step) {
            turtle.forward(i);
            turtle.turn(angle);
        }
    }

    /**
     * Main method.
     * <p>
     * This is the method that runs when you run "java TurtleSoup".
     *
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

//        drawSquare(turtle, 40);
        drawPersonalArt(turtle);

        // draw the window
        turtle.draw();
    }

}
