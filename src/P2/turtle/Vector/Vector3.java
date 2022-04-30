package turtle.Vector;

public class Vector3 {
    private double x;
    private double y;
    private double z;
    private double norm;

    Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.norm = Math.sqrt(x * x + y * y + z * z);
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }
}
