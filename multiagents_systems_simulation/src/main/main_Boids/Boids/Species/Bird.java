package main.main_Boids.Boids.Species;
import java.awt.Color;
import main.main_Boids.Behaviors.*;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.*;

public class Bird extends Boid {
    private String predator;
    
    public Bird(Vector2D position, Vector2D velocity, Vector2D acceleration, double speedlimit, double forceLimit, 
                double wanderRadius, double pathRadius, int boidSize, Color color, Color compassColor, 
                double angleDistance, int windowWidth, int windowHeight) {
        // we always use the tuned version of Boid for a given species
        super(position, velocity, acceleration, speedlimit, forceLimit, wanderRadius, pathRadius,
              boidSize, color, compassColor, angleDistance, windowWidth, windowHeight);
        this.wanderFactor = 0.75;
        this.species = "Bird";
        this.initializeBehaviors(windowWidth, windowHeight);
        this.predator = "Eagle";
    }
        

    public void initializeBehaviors(int windowWidth, int windowHeight){
        // Add bird-specific behaviors
        this.behaviors.clear();
        this.behaviors.add(new Separation(1.5, windowWidth, windowHeight, this.closeDistance)); // birds avoid crowding
        this.behaviors.add(new Alignment(1, windowWidth, windowHeight, this.neighborDistance)); // birds align with neighbors just fine
        this.behaviors.add(new Cohesion(0.8, windowWidth, windowHeight, this.neighborDistance)); // birds try to stay close to neighbors but not too much
        this.behaviors.add(new FleeFromPredator(2, this.neighborDistance)); // birds flee from predators
    }
    }

