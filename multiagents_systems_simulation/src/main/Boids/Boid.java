package main.Boids;
import main.Behaviors.*;
import main.Vector_2D;
import main.Grid;
import main.GridType;
import main.Path;
import main.FlowField;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Boid {
    protected Vector_2D position_0;
    protected Vector_2D velocity_0;
    protected Vector_2D acceleration_0;
    protected Vector_2D position;
    protected Vector_2D velocity;
    protected Vector_2D acceleration;
    // Speed limit to avoid excessive speeds (or even instantaneous teleportation)
    protected double speedlimit;
    /*Force limit to avoid excessive forces (or even instantaneous acceleration), in fact
    the force should be at the same scale of the boid's weight otherwise it'll just move
    with unrealistic speed or just be crushed if we're being realistic*/
    protected double forceLimit;
    protected double mass = 1;
    protected int boid_size; // the radius if it is represented by a circle and 1/2 its hight if it's a triangle

    protected double angle_wander = 0.1;
    protected double wander_radius;
    protected double wander_factor;
    protected double path_radius;
    protected double slowRadius;
    protected double angleDistance;
    // for simulations
    protected Color color;
    protected Color compassColor;

    // for separation behavior
    protected double close_distance;
    // for align behavior
    protected double neighbor_distance;

    // For the grid used in separation behavior
    protected int cell_row_sep;
    protected int cell_col_sep;

    // For the grid used in cohesion/alignment (together)
    protected int cell_row_tog;
    protected int cell_col_tog;

    protected ArrayList<Behavior> behaviors = new ArrayList<Behavior>();
    // Constructor
    public Boid(Vector_2D position, Vector_2D velocity, Vector_2D acceleration, double speedlimit, double forceLimit, double wander_radius, 
    double path_radius, int boid_size, Color color, Color compassColor, double angleDistance, int windowWidth, int windowHeight) { 
        /* 
        This function is the constructor of the Balls class
        */
        this.position = new Vector_2D(position.getX(), position.getY());
        this.velocity = new Vector_2D(velocity.getX(), velocity.getY());
        this.acceleration = new Vector_2D(acceleration.getX(), acceleration.getY());
        this.boid_size = boid_size;

        this.speedlimit = speedlimit;
        this.forceLimit = forceLimit;
        
        // We need to keep track of the initial positions of the balls for reseting, so we create copies of the input vectors
        this.position_0 = new Vector_2D(position.getX(), position.getY());
        this.velocity_0 = new Vector_2D(velocity.getX(), velocity.getY());
        this.acceleration_0 = new Vector_2D(acceleration.getX(), acceleration.getY());
        this.wander_radius = wander_radius;
        this.path_radius = path_radius;
        this.slowRadius = 1.5*Math.pow(speedlimit,2)/(2*forceLimit);

        this.color = color;
        this.compassColor = compassColor;

        this.close_distance = this.boid_size * 10; // Separation distance based on boid size
        this.TuneDistances(windowWidth, windowHeight, boid_size);

        this.angleDistance = angleDistance;
        
    }

    public Boid(Vector_2D position, Vector_2D velocity, Vector_2D acceleration, double speedlimit, double forceLimit,
      double wander_radius, double path_radius, int boid_size, Color color, Color compassColor, double angleDistance) {
        this(position, velocity, acceleration, speedlimit, forceLimit, wander_radius, path_radius, boid_size, color, compassColor,
         angleDistance, 0,0);
        this.close_distance = 6*this.boid_size;
        this.neighbor_distance = 6*this.boid_size + 40;
        
     }

    public void TuneDistances(int windowWidth, int windowHeight, int boidSize) {
        double diag = Math.sqrt(windowWidth*windowWidth + windowHeight*windowHeight);

        // Tunable constants
        double k_s = 10.0;     // size contribution for separation
        double k_d = 0.01;    // window contribution for separation
        double k_s2 = 6.0;    // size contribution for sight
        double k_d2 = 0.04;   // window contribution for sight

        this.close_distance = boidSize * k_s + diag * k_d; 
        this.neighbor_distance = boidSize * k_s2 + diag * k_d2;

        System.out.println("close_distance = " + this.close_distance +
                        ", neighbor_distance = " + this.neighbor_distance);
    }

////////////////////////////////////////////// Getters /////////////////////////////////////////////
    
    public Vector_2D getPosition() {
        return this.position;
    }
    
    public Vector_2D getVelocity() {
        return this.velocity;
    }
    
    public Vector_2D getAcceleration() {
        return this.acceleration;
    }

    public double getSpeedlimit() {
        return this.speedlimit;
    }

    public double getforceLimit(){
        return this.forceLimit;
    }

    public int getSize(){
        return this.boid_size;
    }

    public Color getColor() {
    return color;
    }

    public Color getCompassColor() {
    return compassColor;
    }

    public double getNeighbor_distance(){
        return this.neighbor_distance;
    }

    public double getClose_distance(){
        return this.close_distance;
    }

    public double getWander_radius(){
        return this.wander_radius;
    }

    public double getPath_radius(){
        return this.path_radius;
    }

    public double getSlowRadius(){
        return this.slowRadius;
    }

    public double getAngleDistance(){
        return this.angleDistance;
    }

    public double getMass(){
        return this.mass;
    }

    public void setCellSeparation(int row, int col) {
    this.cell_row_sep = row;
    this.cell_col_sep = col;
    }
    public int getCellRowSeparation() { 
        return this.cell_row_sep; }
    public int getCellColSeparation() { 
        return this.cell_col_sep; }

    public void setCellTogether(int row, int col) {
        this.cell_row_tog = row;
        this.cell_col_tog = col;
    }
    public int getCellRowTogether() { 
        return this.cell_row_tog; }
    public int getCellColTogether() { 
        return this.cell_col_tog; }

    public ArrayList<Behavior> getBehaviors(){
        return this.behaviors;
    }


    /////////////////////////////////////// Methods /////////////////////////////////////////////
    public void applyForce(Vector_2D force) {
        // Newton’s second law, but with force accumulation, adding all input forces to acceleration
        force.limit(forceLimit);
        double actualX = this.acceleration.getX();
        double actualY = this.acceleration.getY();
        this.acceleration = new Vector_2D(actualX + force.getX()/this.mass,actualY + force.getY()/this.mass);

    }


    public Vector_2D getSteeringForce(Vector_2D desired) {
        /*Calculate the steering force towards a desired velocity, this is an alternative to the gravitational force
         that allows more precise control of the boid's movement, in fact a simple gravitation force will just pull the boid
         regardless of its motion direction*/
        Vector_2D steer = new Vector_2D(desired.getX(), desired.getY());
        steer.subtract(this.velocity);
        return steer;
    }

    public boolean inSight(Boid other){
        // Implements the innsight vision ;
        double distance = this.distance_to_optimized(other);
        Vector_2D AB = other.position.copy();
        AB.subtract(this.position);
        double angle = AB.heading2();
        if (distance > this.neighbor_distance*this.neighbor_distance){
            return false;
        }else if (Math.PI - Math.abs(angle) < angleDistance/2) {
            return false;
        }else{
            return true;
        }
    }
    
    public Vector_2D target_path(Vector_2D start,Vector_2D end){
        // Make the boid follow the segement [start,end]
         Vector_2D future_pos = future_pos();
         Vector_2D normal_point = future_pos.getNormalPoint(start,end);
         double distance = normal_point.getdistance(future_pos);
         if (distance > path_radius){
            return normal_point;
         }else{
            return end;
         }
    }

    
    public Vector_2D getDesiredDirection(Vector_2D target) {
        // Calculate the desired direction towards a target position with a target_raduis
        target = target_path(this.position_0,target);
        Vector_2D desired = new Vector_2D(target.getX() , target.getY() );
        desired.subtract(this.position);
        double distance = this.position.getdistance(target);
        if (distance < this.slowRadius){
            desired.updateMagnitude(this.speedlimit*distance/this.slowRadius);
            //wander_radius *= Math.pow((distance/this.slowRadius),2); // Lowering the circle
                                                                   // raduis when we are near
                                                                   // the target

        }else{
            desired.updateMagnitude(this.speedlimit); 
        }
        return desired;
    }
    
    public Vector_2D getDesiredDirection2(Vector_2D target) {
        // Calculate the desired direction towards a target position without a target_radius
        target = target_path(this.position_0,target);
        Vector_2D desired = new Vector_2D(target.getX() , target.getY() );
        desired.subtract(this.position);
        return desired;
    }
    
    public void FlowMov(FlowField field){
        // Movement in a flowfield
        Vector_2D future_position = future_pos();

        Vector_2D future_desired = field.get_vector(future_position);
        Vector_2D actual_steer = getSteeringForce(future_desired);
        actual_steer.limit(speedlimit);
        this.applyForce(actual_steer);
    }

    public Vector_2D wander(double forceFactor){
        // Implement the wander movement
        Vector_2D future_position = future_pos();
        
        Random rand = new Random();
        double angle = rand.nextDouble()*Math.PI + angle_wander;
        Vector_2D green_point = new Vector_2D(wander_radius*Math.cos(angle),wander_radius*Math.sin(angle));
        future_position.add(green_point);
        
        Vector_2D desired_wander = getDesiredDirection(future_position);
        Vector_2D steerWander = getSteeringForce(desired_wander);
        steerWander.limit(forceLimit);
      return steerWander;
    }
    public Vector_2D wander(){
        // Implement the wander movement without a forceFactor
        return wander(1);
    }
    public void follow_path(Path path){
        int taille = path.getTaille();
        ArrayList<Vector_2D> tableauPoints = path.gettableauPoints();
        Vector_2D futurePosition = future_pos();
        double smallestDistance = Double.POSITIVE_INFINITY;
        Vector_2D actualTarget = new Vector_2D();
       for (int i = 0;i<taille-1;i++){
          Vector_2D start = tableauPoints.get(i);
          Vector_2D end = tableauPoints.get(i+1);
          Vector_2D normalPoint = futurePosition.getNormalPoint(start, end);
          double maxX = Math.max(start.getX(),end.getX());
          double minX = Math.min(start.getX(),end.getX());
          double nX = normalPoint.getX();
          if (nX < minX || nX > maxX ){
            normalPoint = new Vector_2D(end.getX(),end.getY());
          }
          double distance = futurePosition.getdistance(normalPoint);
          if (distance < smallestDistance){
            smallestDistance = distance;
            actualTarget = normalPoint;
          }
       }
       
    }

    public Vector_2D future_pos_steer(Vector_2D steer){
        // Calculate the future position with a steering force
        steer.limit(forceLimit);
        Vector_2D future_position = new Vector_2D(0,0);
        future_position.add(steer,this.velocity);
        future_position.limit(speedlimit);
        future_position.add(this.position);
        return future_position ;
    }

    public Vector_2D seek(Vector_2D target, double factor) {
        // Seek towards a target position in a realistic manner
        Vector_2D desired = getDesiredDirection(target);
        Vector_2D steer = getSteeringForce(desired);
        steer.multiply(factor);
        steer.limit(this.forceLimit);
        /* this.applyForce(steer) */;
        return steer; // return the steering force this will allow us to apply all the forces at once
    }

    public Vector_2D seek(Vector_2D target){
        return seek(target,1);
    }

    
    public Vector_2D future_pos(){
        // Calculating thbe future position with the current velocity
        Vector_2D future_position = new Vector_2D();
        future_position.add(this.velocity,this.position);
        return future_position;   
    }



    public void updatestate(){
        // Update velocity
        this.velocity.add(this.acceleration);
        // Limit speed
        this.velocity.limit(this.speedlimit);
        // Update position
        this.position.add(this.velocity);
        // Reset acceleration for the next frame
        this.acceleration.multiply(0.0);
    }
    
    public void reInit() {
        this.position = new Vector_2D(this.position_0.getX(),this.position_0.getY());
        this.velocity = new Vector_2D(this.velocity_0.getX(),this.velocity_0.getY());
        this.acceleration = new Vector_2D(this.acceleration_0.getX(),this.acceleration_0.getY());
    }
    public double distance_to(Boid other){     
        Vector_2D X = this.position;
        Vector_2D Y = other.position;
        
        return X.getdistance(Y);
    }

     public double distance_to_optimized(Boid other){
        // Optimized version without square root
        double x_this = this.position.getX();
        double y_this = this.position.getY();
        double x_other = other.position.getX();
        double y_other = other.position.getY();

        double diffX = x_this - x_other;
        double diffY = y_this - y_other;
        
        return diffX*diffX + diffY*diffY;
    } 
////////////////////////////////////////////// Group Behavior /////////////////////////////////////////////
    public void submittoGroupBehavior(HashMap<GridType, Grid> grids) {
        /* Apply all group behavior forces at once */
        /* Vector_2D separation = this.separation(grid_separation);
        Vector_2D alignement = this.alignment(grid_together);
        Vector_2D cohesion   = this.cohesion(grid_together);
        
        
        this.applyForce(separation);
        this.applyForce(alignement);
        this.applyForce(cohesion);*/
        for (Behavior behavior : behaviors) {
            Grid grid = grids.get(behavior.getGridType());
            Vector_2D force = behavior.behave(this, grid); // Assuming BehaviorOnGrids is not used here
            this.applyForce(force);
        }
        // all beings wander
        Vector_2D wanderForce = this.wander(this.wander_factor);
        this.applyForce(wanderForce);
    } 
}
