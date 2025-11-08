package main;
import java.awt.Color;
import java.util.ArrayList;
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
    private int boid_size; // the radius if it is represented by a circle and 1/2 its hight if it's a triangle
    private double angle_wander = Math.PI/2;
    private double wander_radius;
    private double path_radius;
    private double slowRadius;
    private double angleDistance;
    // for simulations
    private Color color;
    private Color compassColor;

    // for separation behavior
    private double close_distance;
    // for align behavior
    private double neighbor_distance;
    // Constructor
    public Boid(Vector_2D position, Vector_2D velocity, Vector_2D acceleration, double speedlimit, double forcelimit, double wander_radius, double path_radius, int boid_size, Color color, Color compassColor, double angleDistance) { 
        /* 
        This function is the constructor of the Balls class
        */
        this.position = new Vector_2D(position.getX(), position.getY());
        this.velocity = new Vector_2D(velocity.getX(), velocity.getY());
        this.acceleration = new Vector_2D(acceleration.getX(), acceleration.getY());
        this.boid_size = boid_size;

        this.speedlimit = speedlimit;
        this.forcelimit = forcelimit;
        
        // We need to keep track of the initial positions of the balls for reseting, so we create copies of the input vectors
        this.position_0 = new Vector_2D(position.getX(), position.getY());
        this.velocity_0 = new Vector_2D(velocity.getX(), velocity.getY());
        this.acceleration_0 = new Vector_2D(acceleration.getX(), acceleration.getY());
        this.wander_radius = wander_radius;
        this.path_radius = path_radius;
        this.slowRadius = 1.5*Math.pow(speedlimit,2)/(2*forcelimit);

        this.color = color;
        this.compassColor = compassColor;

        this.close_distance = this.boid_size * 7; // Separation distance based on boid size
        this.angleDistance = angleDistance;
        this.neighbor_distance = this.boid_size * 6 + 70; // the bigger you are the more you see, but you still have to see something even if you're small
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

    public double getForceLimit(){
        return this.forcelimit;
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

    public void setCloseDistance(double new_close_distance){
        if (new_close_distance > 0){
            this.close_distance = new_close_distance;
        }
        else{
            System.out.println("Distance must be positive");
        }
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


    //////////////////////////// Forces ////////////////////////////
    public void applyForce(Vector_2D force) {
        // Newton’s second law, but with force accumulation, adding all input forces to acceleration
        force.limit(forcelimit);
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

    public boolean inSight(Boid other){
        // Implements the innsight vision ;
        double distance = this.position.getdistance(other.position);
        Vector_2D AB = other.position.copy();
        AB.subtract(this.position);
        double angle = AB.heading2();
        if (distance > this.neighbor_distance){
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
            wander_radius *= Math.pow((distance/this.slowRadius),2); // Lowering the circle
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

    public void wander(Vector_2D target, double forceFactor){
        // Implement the wander movement
        Vector_2D desired = getDesiredDirection(target);
        Vector_2D steer = getSteeringForce(desired);
        Vector_2D future_position = future_pos();
        
        Random rand = new Random();
        double angle = rand.nextDouble()*Math.PI + angle_wander;
        Vector_2D green_point = new Vector_2D(wander_radius*Math.cos(angle),wander_radius*Math.sin(angle));
        future_position.add(green_point);
        
        Vector_2D desired_wander = getDesiredDirection2(future_position);
        Vector_2D steer_wander = getSteeringForce(desired_wander);
        steer.multiply(0.6);
        steer_wander.multiply(0.9);
        steer.add(steer_wander);
        steer.multiply(forceFactor);
        steer.limit(forcelimit);
        this.applyForce(steer);
    }
    public void wander(Vector_2D target){
        // Implement the wander movement
        wander(target,1);
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
       wander(actualTarget);
    }

    public Vector_2D future_pos_steer(Vector_2D steer){
        // Calculate the future position with a steering force
        steer.limit(forcelimit);
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
        steer.limit(this.forcelimit);
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
        this.position.setX(this.position_0.getX());
        this.position.setY(this.position_0.getY());
        this.velocity.setX(this.velocity_0.getX());
        this.velocity.setY(this.velocity_0.getY());
        this.acceleration.setX(this.acceleration_0.getX());
        this.acceleration.setY(this.acceleration_0.getY());
}

    public double distance_to(Boid other){     
        Vector_2D X = this.position;
        Vector_2D Y = other.position;
        
        return X.getdistance(Y);
    }

/*     public double distance_to_optimized(Boid other){
        // Optimized version without square root
        double x_this = this.position.getX();
        double y_this = this.position.getY();
        double x_other = other.position.getX();
        double y_other = other.position.getY();

        double diffX = x_this - x_other;
        double diffY = y_this - y_other;
        
        return diffX*diffX + diffY*diffY;
    } */

    public Vector_2D separation(Boids boids,double forceFactor){
        int n_close_boids = 0;
        Vector_2D average_flee = new Vector_2D(); // intiialize to an empty vector
        
        ArrayList<Boid> listBoids = boids.getlisteBoids();
        for (Boid otherboid : listBoids){
            if ((otherboid != this) && 
            (distance_to(otherboid) < this.close_distance)){
                // update n_close_boids
                n_close_boids += 1;
                Vector_2D from_me_to_you = this.getPosition().copy();
                from_me_to_you.subtract(otherboid.getPosition());

                // the closer boid is to other the more it is urging to flee away
                double dist = this.distance_to(otherboid);

                if (dist > 0) {
                    from_me_to_you.updateMagnitude(1.0 / dist);}
                // update average_flee
                average_flee.add(from_me_to_you);
                
            }
        }
        
        if (n_close_boids >= 1){
            // divide by the number of close boids to get the average flee
            average_flee.divide(n_close_boids);
            // the boid wants to flee as fast as possible in the direction of the average_velocity vector
            average_flee.updateMagnitude(this.getSpeedlimit());
            Vector_2D separ_force = this.getSteeringForce(average_flee);
            separ_force.multiply(forceFactor);
            separ_force.limit(this.getForceLimit());

            // return separation force
            return separ_force;
        }
        return new Vector_2D(0,0);// no close boids detected
    }

    public Vector_2D separation(Boids boids){
       return separation(boids,1);
    }



    public Vector_2D cohesion(Boids boids,double forceFactor){
        Vector_2D average_pos = new Vector_2D();
        double sum_mass = 0;
        ArrayList<Boid> listBoids = boids.getlisteBoids();

        for (Boid otherboid : listBoids){
            if ((otherboid != this) && 
            (this.inSight(otherboid))){
                double m = otherboid.getMass();
                sum_mass += m;

                // Compute the weighted sum of positions
                Vector_2D weighted_pos = otherboid.getPosition().copy();
                weighted_pos.multiply(m);

                // update average_pos
                average_pos.add(weighted_pos);
                
            }
        }
        if (sum_mass > 0){
            // divide by the sum of masses to get the average position (aka the center of inertia)
            average_pos.divide(sum_mass);
            Vector_2D desired = average_pos.copy();
            Vector_2D inertia_seek = this.seek(desired, forceFactor);
            
            return inertia_seek;
        }
        return new Vector_2D(0,0);// no close boids detected
    }
    public Vector_2D cohesion(Boids boids){
        return cohesion(boids,1);
       }



    public Vector_2D alignment(Boids boids,double forceFactor){
        int n_sight_boids = 0;
        Vector_2D average_velocity = new Vector_2D();
        ArrayList<Boid> listBoids = boids.getlisteBoids();

        for (Boid otherboid : listBoids){
            if ((otherboid != this) && 
            (this.inSight(otherboid))){
                // update average_velocity
                average_velocity.add(otherboid.getVelocity());
                n_sight_boids++;
            }
        }

        if (n_sight_boids >= 1){
            // divide by number of close boids to get the average velocity
            average_velocity.divide(n_sight_boids);

            // the boid wants to flee as fast as possible in the direction of the average_flee vector
            average_velocity.updateMagnitude(this.getSpeedlimit());
            Vector_2D align_force = this.getSteeringForce(average_velocity);
            align_force.multiply(forceFactor);
            align_force.limit(this.getForceLimit());

            /* return align_force; */
            return align_force;
        }
        return new Vector_2D(0,0);// no close boids detected
    }
    public Vector_2D alignment(Boids boids){
        return alignment(boids,1);
       }
    
    public void submittoGroupBehavior(Boids boids){
        /* Apply all group behavior forces at once */
        Vector_2D separation = this.separation(boids);
        Vector_2D alignement = this.alignment(boids);
        Vector_2D cohesion   = this.cohesion(boids);
        this.applyForce(separation);
        /* this.applyForce(alignement);  */
        this.applyForce(cohesion);
    }
}


