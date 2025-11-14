package main.Boidutils;

public class Vector_2D {
    private double x;  
    private double y;

    public Vector_2D(double x, double y) {
        // Constructor for a vector with given x and y components
        this.x = x;
        this.y = y;
    }

    public Vector_2D(){
        // Constructor for a zero vector, it is the default setting in java if this.x and this.y are not specified, but to be explicit:
        this.x = 0;
        this.y = 0;
    }
    public Vector_2D copy(){
        return new Vector_2D(this.x,this.y);
    }
    public double getMagnitude() {
        return Math.sqrt(x*x + y*y);
    }
    public double getdistance(Vector_2D other){
        Vector_2D X = new Vector_2D(this.x - other.x, this.y - other.y);
        return X.getMagnitude();
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
        this.x *= magnitude;
        this.y *= magnitude;
    }
    public void add(Vector_2D v1, Vector_2D v2){
        this.x = v1.x + v2.x;
        this.y = v1.y + v2.y;
    }

    public void add(Vector_2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void subtract(Vector_2D other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    public void subtract2New(Vector_2D v1, Vector_2D v2){
        this.x = v2.x - v1.x;
        this.y = v2.y - v1.y;
    }

    public void multiply(double scalar) {
            this.x *= scalar;
            this.y *= scalar;
    }

    public void divide(double scalar) {
        if (scalar != 0){
        this.x /= scalar;
        this.y /= scalar;
        }
    }

    public  void limit(double max) {
        // Limit the magnitude of the vector to a maximum value
        if (this.getMagnitude() > max) {
            this.updateMagnitude(max);
        }
    }


    public double heading() {
        return Math.atan2(this.y, this.x) ;
    }
    public double heading2() {
        return Math.atan2(this.x, this.y) ;
    }
    public double dot(Vector_2D other){
        return this.x*other.x+this.y*other.y;
    }

    public Vector_2D getNormalPoint(Vector_2D start, Vector_2D end){
        // Return the normal projection of self on the segment [start, end]
        Vector_2D a = new Vector_2D(this.x,this.y );
        a.subtract(start);
        Vector_2D b = new Vector_2D(end.x,end.y);
        
        b.subtract(start);
        b.normalize();
        b.updateMagnitude(a.dot(b));
        
        Vector_2D normal_point = new Vector_2D(start.x+b.x, start.y+b.y);
        
        return normal_point;
    }

    public void rotate(double angle){
        double[][] Rotation_matrix = {{Math.cos(angle), -Math.sin(angle)},
                                      {Math.sin(angle), Math.cos(angle)}};
        double x_old = this.x;
        double y_old = this.y;
        this.x = Rotation_matrix[0][0]* x_old + Rotation_matrix[0][1]* y_old;
        this.y = Rotation_matrix[1][0]* x_old + Rotation_matrix[1][1]* y_old;
    }

    // getters
    public double getX() { return x; }
    public double getY() { return y; }

    @Override
    public String toString(){
        return "(" + Double.toString(this.x) + "," + Double.toString(this.y) + ")" ;
    }

    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

} 