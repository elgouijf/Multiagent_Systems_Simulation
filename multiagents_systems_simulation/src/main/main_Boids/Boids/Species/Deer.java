package main.main_Boids.Boids.Species;


import java.awt.Color;
import java.util.Random;
import main.main_Boids.Behaviors.*;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.Path;
import main.main_Boids.Boidutils.Vector2D;

public class Deer extends Boid{

    private String predator;


    public Deer(Vector2D position, Vector2D velocity, Vector2D acceleration, double speedlimit, double forceLimit, 
                double wanderRadius, double pathRadius, int boidSize, Color color, Color compassColor, 
                double angleDistance, int windowWidth, int windowHeight){
        super(position, velocity, acceleration, speedlimit, forceLimit, wanderRadius, pathRadius,
              boidSize, color, compassColor, angleDistance, windowWidth, windowHeight);
        this.species = "Deer";

        // Tries to avoid others and predators;
        this.wanderRadius = 3;
        this.initializeBehaviors(windowWidth, windowHeight);
        this.path = new Path(pathRadius);
        // Initialize a random path
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            double x = rand.nextDouble() * windowWidth;
            double y = rand.nextDouble() * windowHeight;
            this.path.add(new Vector2D(x, y));
        }

        this.predator = "Wolf"; // Wolves are the predators of Deers
                }

    public void initializeBehaviors(int width, int height) {
        
        this.behaviors.clear();
        // they dont like being 
        this.behaviors.add(new Separation(1, width, height, this.closeDistance * 1.5));

        // strongest – survival first
        this.behaviors.add(new FleeFromPredator(3.5,Math.pow(this.neighborDistance,2)));

        // keeps migration direction
        this.behaviors.add(new FollowPath(2,100.0,100));
    }      
}