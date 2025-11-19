package main.main_Boids.Boids.Species;
import java.awt.Color;
import main.main_Boids.Behaviors.*;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.*;

public class Bird extends Boid {
    private String predator;
    
    public Bird(Vector_2D position, Vector_2D velocity, Vector_2D acceleration, double speedlimit, double forceLimit, 
                double wander_radius, double path_radius, int boid_size, Color color, Color compassColor, 
                double angleDistance, int windowWidth, int windowHeight) {
        // we always use the tuned version of Boid for a given species
        super(position, velocity, acceleration, speedlimit, forceLimit, wander_radius, path_radius,
              boid_size, color, compassColor, angleDistance, windowWidth, windowHeight);
        this.wander_factor = 0.75;
        this.species = "Bird";
        this.initializeBehaviors(windowWidth, windowHeight);
        this.predator = "Eagle";
    }
        

    public void initializeBehaviors(int windowWidth, int windowHeight){
        // Add bird-specific behaviors
        this.behaviors.clear();
        this.behaviors.add(new Separation(1.5, windowWidth, windowHeight, this.close_distance)); // birds avoid crowding
        this.behaviors.add(new Alignment(windowWidth, windowHeight, this.neighbor_distance)); // birds align with neighbors just fine
        this.behaviors.add(new Cohesion(0.8, windowWidth, windowHeight, this.neighbor_distance)); // birds try to stay close to neighbors but not too much
        this.behaviors.add(new FleeFromPredator(2, this.neighbor_distance * 2)); // birds flee from predators
    }
    }

