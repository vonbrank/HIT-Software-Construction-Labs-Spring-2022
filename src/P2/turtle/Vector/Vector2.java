package turtle.Vector;

import turtle.Point;

public class Vector2 {
    private double x;
    private double y;
    private double norm;

    /**
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
        norm = Math.sqrt(x * x + y * y);
    }

    /**
     * construct a Vector2 by two point which is from source to target
     * @param source the sourcePoint
     * @param target the targetPoint
     */
    public Vector2(Point source, Point target) {
        x = target.x() - source.x();
        y = target.y() - source.y();
        norm = Math.sqrt(x * x + y * y);
    }

    /**
     * @return x-coordinate of the vector
     */
    public double x() {
        return x;
    }

    /**
     * @return y-coordinate of the vector
     */
    public double y() {
        return y;
    }

    /**
     * @return norm, aka length of the vector
     */
    public double norm() {
        return norm;
    }

    /**
     * Make the vector instance normalized
     *
     * @return the instance of the Vector2
     */
    public Vector2 normalize() {
        x /= norm;
        y /= norm;
        norm = 1;
        return this;
    }

    /**
     * @return the normalized version of the Vector2
     * and does not mutate the current one
     */
    public Vector2 getNormalized() {
        return new Vector2(x / norm, y / norm);
    }

    public Vector2 subtract(Vector2 a, Vector2 b) {
        return new Vector2(a.x - b.x, a.y - b.y);
    }

    /**
     * @param a the first Vector2 to evaluate
     * @param b the second Vector2 to evaluate
     * @return The dot product of a and b
     */
    public static double dot(Vector2 a, Vector2 b) {
        return a.x * b.x + a.y * b.y;
    }

    public static Vector3 cross(Vector2 a, Vector2 b) {
        return new Vector3(0, 0, a.x * b.y - b.x * a.y);
    }

    /**
     * @param a the first Vector2 to evaluate
     * @param b the second Vector2 to evaluate
     * @return return the angle from a to b in anticlockwise
     * satisfying: 0 <= angle <= 360
     */
    public static double getAngleDiff(Vector2 a, Vector2 b) {
        double angleDiff = Math.acos(Vector2.dot(a.getNormalized(), b.getNormalized())) * 180.0 / Math.PI;
        if (Vector2.cross(a, b).z() < 0) angleDiff = -angleDiff;
        if (angleDiff < 0) angleDiff += 360;
        return angleDiff;
    }

    @Override
    public String toString() {
        return String.format("[%f, %f]", x, y);
    }
}
