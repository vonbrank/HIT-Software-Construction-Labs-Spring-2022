package turtle.Vector;

public class Vector2 {
    private double x;
    private double y;
    private double norm;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
        norm = Math.sqrt(x * x + y * y);
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double norm() {
        return norm;
    }

    /**
     * Make the vector instance normalized
     */
    public Vector2 normalize() {
        x /= norm;
        y /= norm;
        return this;
    }

    public Vector2 getNormalized() {
        return new Vector2(x / norm, y / norm);
    }

    public static Vector2 dot(Vector2 a, Vector2 b) {
        return new Vector2(a.x * b.x, a.y * b.y);
    }
}
