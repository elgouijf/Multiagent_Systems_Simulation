package main;
import java.util.Random;
public class Boid {
    private Vector_2D position_0;
    private Vector_2D velocity_0;
    private Vector_2D acceleration_0;
    private Vector_2D position;
    private Vector_2D velocity;
    private Vector_2D acceleration;
    // Speed limit to avoid excessive speeds (or even instantaneous teleportation)
    private double speedlimit;
    /*Force limit to avoid excessive forces (or even instantaneous acceleration), in fact
    the force should be at the same scale of the boid's weight otherwise it'll just move
    with unrealistic speed or just be crushed if we're being realistic*/
    private double forcelimit;
    private double mass = 1;
    private double boid_radius;
    private double angle_wander = Math.PI/6;
    private double wander_radius;

    // Constructor
    public Boid(Vector_2D position, Vector_2D velocity, Vector_2D acceleration, double speedlimit, double forcelimit, double wander_radius) { 
        /* 
        This function is the constructor of the Balls class
        */
        this.position = new Vector_2D(position.getX(), position.getY());
        this.velocity = new Vector_2D(velocity.getX(), velocity.getY());
        this.acceleration = new Vector_2D(acceleration.getX(), acceleration.getY());

        this.speedlimit = speedlimit;
        this.forcelimit = forcelimit;
        
        // We need to keep track of the initial positions of the balls for reseting, so we create copies of the input vectors
        this.position_0 = new Vector_2D(position.getX(), position.getY());
        this.velocity_0 = new Vector_2D(velocity.getX(), velocity.getY());
        this.acceleration_0 = new Vector_2D(acceleration.getX(), acceleration.getY());
        this.wander_radius = wander_radius;
    }
    
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
    //////////////////////////// Forces ////////////////////////////
    public void applyForce(Vector_2D force) {
        // Newton’s second law, but with force accumulation, adding all input forces to acceleration
        double actualX = this.acceleration.getX();
        double actualY = this.acceleration.getY();
        this.acceleration.setX(actualX + force.getX()/this.mass);
        this.acceleration.setY(actualY + force.getY()/this.mass);

    }


    public Vector_2D getSteeringForce(Vector_2D desired) {
        /*Calculate the steering force towards a desired velocity, this is an alternative to the gravitational force
         that allows more precise control of the boid's movement, in fact a simple gravitation force will just pull the boid
         regardless of its motion direction*/
        Vector_2D steer = new Vector_2D(desired.getX(), desired.getY());
        steer.subtract(this.velocity);
        return steer;
    }
    

    
    public Vector_2D getDesiredDirection(Vector_2D target, double target_radius) {
        // Calculate the desired direction towards a target position
        Vector_2D desired = new Vector_2D(target.getX() , target.getY() );
        desired.subtract(this.position);
        Random rand = new Random();
        double angle = rand.nextDouble()*Math.PI/6 + angle_wander;
        double distance = this.position.getdistance(target);
        if (distance < target_radius){
            desired.updateMagnitude(this.speedlimit*distance/target_radius);
            wander_radius *= distance/target_radius;

        }else{
            desired.updateMagnitude(this.speedlimit); 
        }
        Vector_2D green_point = new Vector_2D(wander_radius*Math.cos(angle),wander_radius*Math.sin(angle));
        desired.add(green_point);
        return desired;
    }
    public void FlowMov(FlowField field){
        // Movement in a flowfield
        Vector_2D future_position = new Vector_2D(0,0);
        future_position.add(this.velocity,this.position);

        Vector_2D future_desired = field.get_vector(future_position);
        Vector_2D actual_steer = getSteeringForce(future_desired);
        actual_steer.limit(speedlimit);
        this.applyForce(actual_steer);
    }
    
    public Vector_2D future_pos(Vector_2D steer){
        // Calculate the future position with a steering force
        steer.limit(forcelimit);
        Vector_2D future_position = new Vector_2D(0,0);
        future_position.add(steer,this.velocity);
        future_position.limit(speedlimit);
        future_position.add(this.position);
        return future_position ;
    }

    public void seek(Vector_2D target,double target_radius) {
        // Seek towards a target position
        Vector_2D desired = getDesiredDirection(target,target_radius);
        Vector_2D steer = getSteeringForce(desired);
        steer.limit(this.forcelimit);
        this.applyForce(steer);
        
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
        this.position.setX(this.position_0.getX());
        this.position.setY(this.position_0.getY());
        this.velocity.setX(this.velocity_0.getX());
        this.velocity.setY(this.velocity_0.getY());
        this.acceleration.setX(this.acceleration_0.getX());
        this.acceleration.setY(this.acceleration_0.getY());
}


}


