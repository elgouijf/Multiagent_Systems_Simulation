package main.Boids.Species;
import main.Boids.Boid;
import main.Boidutils.*;
import main.Behaviors.*;
import java.awt.Color;

public class Bird extends Boid {
    public Bird(Vector_2D position, Vector_2D velocity, Vector_2D acceleration, double speedlimit, double forceLimit, 
                double wander_radius, double path_radius, int boid_size, Color color, Color compassColor, 
                double angleDistance, int windowWidth, int windowHeight) {
        // we always use the tuned version of Boid for a given species
        super(position, velocity, acceleration, speedlimit, forceLimit, wander_radius, path_radius,
              boid_size, color, compassColor, angleDistance, windowWidth, windowHeight);
        this.wander_factor = 0.75;
        this.initializeBehaviors(windowWidth, windowHeight);}
        

    public void initializeBehaviors(int windowWidth, int windowHeight){
        // Add bird-specific behaviors
        this.behaviors.add(new Separation(1.5, windowWidth, windowHeight, this.close_distance)); // birds avoid crowding
        this.behaviors.add(new Alignment(windowWidth, windowHeight, this.neighbor_distance)); // birds align with neighbors just fine
        this.behaviors.add(new Cohesion(0.8, windowWidth, windowHeight, this.neighbor_distance)); // birds try to stay close to neighbors but not too much
    }
    }

