package main.main_Boids.Boids.Species;


import java.awt.Color;
import main.main_Boids.Behaviors.*;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.Path;
import main.main_Boids.Boidutils.Vector_2D;

public class Deer extends Boid{

    private String predator;


    public Deer(Vector_2D position, Vector_2D velocity, Vector_2D acceleration, double speedlimit, double forceLimit, 
                double wander_radius, double path_radius, int boid_size, Color color, Color compassColor, 
                double angleDistance, int windowWidth, int windowHeight){
        super(position, velocity, acceleration, speedlimit, forceLimit, wander_radius, path_radius,
              boid_size, color, compassColor, angleDistance, windowWidth, windowHeight);
        this.species = "Deer";

        // Tries to avoid others and predators;
        this.wander_radius = 3;
        this.initializeBehaviors(windowWidth, windowHeight);
        this.path = new Path(path_radius);
        this.predator = "Wolf";
                }

    public void initializeBehaviors(int width, int height) {
        
        this.behaviors.clear();
        // they dont like being 
        this.behaviors.add(new Separation(3, width, height, this.close_distance * 1.5));

        // strongest – survival first
        this.behaviors.add(new FleeFromPredator(3.5,Math.pow(this.neighbor_distance,2)));

        // keeps migration direction
        this.behaviors.add(new FollowPath(1.5,1.0,100));
    }      
}