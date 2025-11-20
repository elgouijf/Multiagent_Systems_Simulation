package main.main_Boids.Boids;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import main.main_Boids.Behaviors.*;
import main.main_Boids.Boidutils.*;

public class Boid {
    protected Vector2D position0;
    protected Vector2D velocity0;
    protected Vector2D acceleration0;
    protected Vector2D position;
    protected Vector2D velocity;
    protected Vector2D acceleration;
    // Speed limit to avoid excessive speeds (or even instantaneous teleportation)
    protected double speedlimit;
    /*Force limit to avoid excessive forces (or even instantaneous acceleration), in fact
    the force should be at the same scale of the boid's weight otherwise it'll just move
    with unrealistic speed or just be crushed if we're being realistic*/
    protected double forceLimit;
    protected double mass = 1;
    protected int boidSize; // the radius if it is represented by a circle and 1/2 its hight if it's a triangle

    protected double angleWander = 0.1;
    protected double wanderRadius;
    protected double wanderFactor;
    protected double pathRadius;
    protected double slowRadius;
    protected double angleDistance;
    // for simulations
    protected Color color;
    protected Color compassColor;
    protected String prey = "Boid";

    // for separation behavior
    protected double closeDistance;
    // for align behavior
    protected double neighborDistance;

    // For the grid used in separation behavior
    protected int cellRowSep;
    protected int cellColSep;

    // For the grid used in cohesion/alignment (together)
    protected int cellRowTog;
    protected int cellColTog;

    protected ArrayList<Behavior> behaviors = new ArrayList<Behavior>();

    protected String species = "Boid"; // valeur par défaut
    protected Path path = null;   // valeur par défaut

    // Constructor
    public Boid(Vector2D position, Vector2D velocity, Vector2D acceleration, double speedlimit, double forceLimit, double wanderRadius, 
    double pathRadius, int boidSize, Color color, Color compassColor, double angleDistance, int windowWidth, int windowHeight) { 
        /* 
        This function is the constructor of the Balls class
        */
        this.position = new Vector2D(position.getX(), position.getY());
        this.velocity = new Vector2D(velocity.getX(), velocity.getY());
        this.acceleration = new Vector2D(acceleration.getX(), acceleration.getY());
        this.boidSize = boidSize;

        this.speedlimit = speedlimit;
        this.forceLimit = forceLimit;
        
        // We need to keep track of the initial positions of the balls for reseting, so we create copies of the input vectors
        this.position0 = new Vector2D(position.getX(), position.getY());
        this.velocity0 = new Vector2D(velocity.getX(), velocity.getY());
        this.acceleration0 = new Vector2D(acceleration.getX(), acceleration.getY());
        this.wanderRadius = wanderRadius;
        this.pathRadius = pathRadius;
        this.slowRadius = 1.5*Math.pow(speedlimit,2)/(2*forceLimit);

        this.color = color;
        this.compassColor = compassColor;

        this.closeDistance = this.boidSize * 10; // Separation distance based on boid size
        this.TuneDistances(windowWidth, windowHeight, boidSize);

        this.angleDistance = angleDistance;
        this.behaviors.add(new Separation(windowWidth, windowHeight, this.closeDistance));
        this.behaviors.add(new Alignment(windowWidth, windowHeight, this.neighborDistance));
        this.behaviors.add(new Cohesion(windowWidth, windowHeight, this.neighborDistance));
    }

    public Boid(Vector2D position, Vector2D velocity, Vector2D acceleration, double speedlimit, double forceLimit,
      double wanderRadius, double pathRadius, int boidSize, Color color, Color compassColor, double angleDistance) {
        this(position, velocity, acceleration, speedlimit, forceLimit, wanderRadius, pathRadius, boidSize, color, compassColor,
         angleDistance, 0,0);
        this.closeDistance = 6*this.boidSize;
        this.neighborDistance = 6*this.boidSize + 40;
        
     }

    public void TuneDistances(int windowWidth, int windowHeight, int boidSize) {
        double diag = Math.sqrt(windowWidth*windowWidth + windowHeight*windowHeight);

        // Tunable constants
        double kS = 10.0;     // size contribution for separation
        double kD = 0.01;    // window contribution for separation
        double kS2 = 6.0;    // size contribution for sight
        double kD2 = 0.04;   // window contribution for sight

        this.closeDistance = boidSize * kS + diag * kD; 
        this.neighborDistance = boidSize * kS2 + diag * kD2;

        /* System.out.println("closeDistance = " + this.closeDistance +
                        ", neighborDistance = " + this.neighborDistance); */
    }

////////////////////////////////////////////// Getters /////////////////////////////////////////////
    
    public String getSpecies() {
        return this.species;
    }

    public Vector2D getPosition() {
        return this.position;
    }
    
    public Vector2D getVelocity() {
        return this.velocity;
    }
    
    public Vector2D getAcceleration() {
        return this.acceleration;
    }

    public double getSpeedlimit() {
        return this.speedlimit;
    }

    public double getforceLimit(){
        return this.forceLimit;
    }

    public int getSize(){
        return this.boidSize;
    }

    public Color getColor() {
    return color;
    }

    public Color getCompassColor() {
    return compassColor;
    }

    public double getNeighborDistance(){
        return this.neighborDistance;
    }

    public double getCloseDistance(){
        return this.closeDistance;
    }

    public double getwanderRadius(){
        return this.wanderRadius;
    }

    public double getpathRadius(){
        return this.pathRadius;
    }
    public Path getPath(){
        return path;
    }

    public double getSlowRadius(){
        return this.slowRadius;
    }

    public String getPrey(){
        return this.prey;
    }

    public double getAngleDistance(){
        return this.angleDistance;
    }

    public double getMass(){
        return this.mass;
    }

    public void setCellSeparation(int row, int col) {
    this.cellRowSep = row;
    this.cellColSep = col;
    }
    public int getCellRowSeparation() { 
        return this.cellRowSep; }
    public int getCellColSeparation() { 
        return this.cellColSep; }

    public void setCellTogether(int row, int col) {
        this.cellRowTog = row;
        this.cellColTog = col;
    }
    public int getCellRowTogether() { 
        return this.cellRowTog; }
    public int getCellColTogether() { 
        return this.cellColTog; }

    public ArrayList<Behavior> getBehaviors(){
        return this.behaviors;
    }


    /////////////////////////////////////// Methods /////////////////////////////////////////////
    public void applyForce(Vector2D force) {
        // Newton’s second law, but with force accumulation, adding all input forces to acceleration
        force.limit(forceLimit);
        double actualX = this.acceleration.getX();
        double actualY = this.acceleration.getY();
        this.acceleration = new Vector2D(actualX + force.getX()/this.mass,actualY + force.getY()/this.mass);

    }


    public Vector2D getSteeringForce(Vector2D desired) {
        /*Calculate the steering force towards a desired velocity, this is an alternative to the gravitational force
         that allows more precise control of the boid's movement, in fact a simple gravitation force will just pull the boid
         regardless of its motion direction*/
        Vector2D steer = new Vector2D(desired.getX(), desired.getY());
        steer.subtract(this.velocity);
        return steer;
    }

    public boolean inSight(Boid other){
        // Implements the innsight vision ;
        double distance = this.distanceToOptimized(other);
        Vector2D AB = other.position.copy();
        AB.subtract(this.position);
        double angle = AB.heading2();
        if (distance > this.neighborDistance*this.neighborDistance){
            return false;
        }else if (Math.PI - Math.abs(angle) < angleDistance/2) {
            return false;
        }else{
            return true;
        }
    }
    
    public Vector2D targetPath(Vector2D start,Vector2D end){
        // Make the boid follow the segement [start,end]
         Vector2D futurePos = futurePos();
         Vector2D normalPoint = futurePos.getNormalPoint(start,end);
         double distance = normalPoint.getdistance(futurePos);
         if (distance > pathRadius){
            return normalPoint;
         }else{
            return end;
         }
    }

    
    public Vector2D getDesiredDirection(Vector2D target) {
        // Calculate the desired direction towards a target position with a target_raduis
        target = targetPath(this.position0,target);
        Vector2D desired = new Vector2D(target.getX() , target.getY() );
        desired.subtract(this.position);
        double distance = this.position.getdistance(target);
        if (distance < this.slowRadius){
            desired.updateMagnitude(this.speedlimit*distance/this.slowRadius);
            //wanderRadius *= Math.pow((distance/this.slowRadius),2); // Lowering the circle
                                                                   // raduis when we are near
                                                                   // the target

        }else{
            desired.updateMagnitude(this.speedlimit); 
        }
        return desired;
    }
    
    public Vector2D getDesiredDirection2(Vector2D target) {
        // Calculate the desired direction towards a target position without a target_radius
        target = targetPath(this.position0,target);
        Vector2D desired = new Vector2D(target.getX() , target.getY() );
        desired.subtract(this.position);
        return desired;
    }
    
    public void FlowMov(FlowField field){
        // Movement in a flowfield
        Vector2D futurePosition = futurePos();

        Vector2D futureDesired = field.getVector(futurePosition);
        Vector2D actualSteer = getSteeringForce(futureDesired);
        actualSteer.limit(speedlimit);
        this.applyForce(actualSteer);
    }

    public Vector2D wander(double forceFactor){
        // Implement the wander movement
        Vector2D futurePosition = futurePos();
        
        Random rand = new Random();
        double angle = rand.nextDouble()*Math.PI + angleWander;
        Vector2D greenPoint = new Vector2D(wanderRadius*Math.cos(angle),wanderRadius*Math.sin(angle));
        futurePosition.add(greenPoint);
        
        Vector2D desiredWander = getDesiredDirection(futurePosition);
        Vector2D steerWander = getSteeringForce(desiredWander);
        steerWander.limit(forceLimit);
      return steerWander;
    }
    public Vector2D wander(){
        // Implement the wander movement without a forceFactor
        return wander(1);
    }
    public Vector2D followPath(double forceFactor){
        int size = this.path.getsize();
        ArrayList<Vector2D> tableauPoints = this.path.gettableauPoints();
        Vector2D futurePosition = futurePos();
        double smallestDistance = Double.POSITIVE_INFINITY;
        Vector2D actualTarget = new Vector2D();
       for (int i = 0;i<size-1;i++){
          Vector2D start = tableauPoints.get(i);
          Vector2D end = tableauPoints.get(i+1);
          Vector2D normalPoint = futurePosition.getNormalPoint(start, end);
          double maxX = Math.max(start.getX(),end.getX());
          double minX = Math.min(start.getX(),end.getX());
          double nX = normalPoint.getX();
          if (nX < minX || nX > maxX ){
            normalPoint = new Vector2D(end.getX(),end.getY());
          }
          double distance = futurePosition.getdistance(normalPoint);
          if (distance < smallestDistance){
            smallestDistance = distance;
            actualTarget = normalPoint;
          }
       }
       return this.seek(actualTarget,forceFactor);
       
    }

    public Vector2D futurePosSteer(Vector2D steer){
        // Calculate the future position if a steering force was applied to the Boid
        steer.limit(forceLimit);
        Vector2D futurePosition = new Vector2D(0,0);
        futurePosition.add(steer,this.velocity);
        futurePosition.limit(speedlimit);
        futurePosition.add(this.position);
        return futurePosition ;
    }

    public Vector2D seek(Vector2D target, double factor) {
        // Seek towards a target position in a realistic manner
        Vector2D desired = getDesiredDirection(target);
        Vector2D steer = getSteeringForce(desired);
        steer.multiply(factor);
        steer.limit(this.forceLimit);
        /* this.applyForce(steer) */;
        return steer; // return the steering force this will allow us to apply all the forces at once
    }

    public Vector2D seek(Vector2D target){
        return seek(target,1);
    }

    
    public Vector2D futurePos(){
        // Calculating thbe future position with the current velocity
        Vector2D futurePosition = new Vector2D();
        futurePosition.add(this.velocity,this.position);
        return futurePosition;   
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
        this.position = new Vector2D(this.position0.getX(),this.position0.getY());
        this.velocity = new Vector2D(this.velocity0.getX(),this.velocity0.getY());
        this.acceleration = new Vector2D(this.acceleration0.getX(),this.acceleration0.getY());
    }
    public double distanceTo(Boid other){     
        Vector2D X = this.position;
        Vector2D Y = other.position;
        
        return X.getdistance(Y);
    }

     public double distanceToOptimized(Boid other){
        // Optimized version without square root
        double xThis = this.position.getX();
        double yThis = this.position.getY();
        double xOther = other.position.getX();
        double yOther = other.position.getY();

        double diffX = xThis - xOther;
        double diffY = yThis - yOther;
        
        return diffX*diffX + diffY*diffY;
    } 

    public void applyFlowField(FlowField field) {
        Vector2D wind = field.getVector(this.position);
        this.applyForce(wind);
}

////////////////////////////////////////////// Group Behavior /////////////////////////////////////////////
    public void submittoGroupBehavior(HashMap<GridType, Grid> grids, FlowField windField) {

        for (Behavior behavior : behaviors) {
            Grid grid = grids.get(behavior.getGridType());
            Vector2D force = behavior.behave(this, grid); // Assuming BehaviorOnGrids is not used here
            this.applyForce(force);
        }
        // all beings wander
        Vector2D wanderForce = this.wander(this.wanderFactor);
        this.applyForce(wanderForce);
         // appliquer le vent seulement s'il existe
        if (windField != null) {
            Vector2D wind = windField.getVector(this.getPosition());

        this.applyForce(wind);
        }
    } 
}
