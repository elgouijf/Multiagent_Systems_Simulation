package main;

public class Vector_2D {
    private double x;  
    private double y;

    public Vector_2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getMagnitude() {
        return Math.sqrt(x*x + y*y);
    }

    public void normalize() {
        double magnitude = this.getMagnitude();
        if (magnitude == 0) {
            this.x = 0;
            this.y = 0;
        }
        else {
            this.x /= magnitude;
            this.y /= magnitude;
        }
    }

    public void updateMagnitude(double magnitude) {
        // Set the magnitude of a vector to a specific value
        this.normalize();
        this.setX(this.getX() * magnitude);
        this.setY(this.getY() * magnitude);
    }

    public void add(Vector_2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void subtract(Vector_2D other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    public void multiply(double scalar) {
        if (scalar != 0) {
            this.x /= scalar;
            this.y /= scalar;
        }
    }

    public void divide(double scalar) {
        this.x /= scalar;
        this.y /= scalar;
    }

    public  void limit(double max) {
        // Limit the magnitude of the vector to a maximum value
        if (this.getMagnitude() > max) {
            this.updateMagnitude(max);
        }
    }


    public double heading() {
        return Math.atan2(y, x);
    }

    // getters
    public double getX() { return x; }
    public double getY() { return y; }

    // simple setters if needed
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}