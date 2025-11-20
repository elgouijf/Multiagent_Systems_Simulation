package main.main_Boids.Boids.Species;

import java.awt.Color;
import java.util.ArrayList;
import main.main_Boids.Behaviors.*;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.*;

public class Eagle extends Boid {
    private double killRadius;

    public Eagle(Vector2D position, Vector2D velocity, Vector2D acceleration,
                 double speedLimit, double forceLimit, double wanderRadius, double pathRadius,
                 int boidSize, Color color, Color compassColor, double angleDistance,
                 int windowWidth, int windowHeight) {

        super(position, velocity, acceleration, speedLimit, forceLimit,
                wanderRadius, pathRadius, boidSize,
                color, compassColor, angleDistance, windowWidth, windowHeight);

        this.species = "Eagle";
        this.killRadius = 6.0;

        // Eagles wander much less
        this.wanderFactor = 0.25;
        this.prey = "Bird";
        this.mass = 4.0;

        initializeBehaviors(windowWidth, windowHeight);
    }

    public void initializeBehaviors(int width, int height) {
        this.behaviors.clear();
        // Eagles avoid bumping but slightly
        this.behaviors.add(new Separation(0.4, width, height, this.closeDistance * 1.5));

        // Very low alignment
        this.behaviors.add(new Alignment(width, height, this.neighborDistance));

        // Very low cohesion
        this.behaviors.add(new Cohesion(0.3, width, height, this.neighborDistance * 1.2));

        // Predation → strongest
        this.behaviors.add(new Chase(2.2, this.neighborDistance * 3));
    }

    public double getPredationDistance() {
        return this.neighborDistance * 3;
    }

    public ArrayList<Boid> hunt(ArrayList<Boid> potentialPrey) {
        ArrayList<Boid> caught = new ArrayList<>();
        for (Boid b : potentialPrey) {
            if (b instanceof Bird) {
                double dSquare = this.distanceToOptimized(b);
                if (dSquare < killRadius*killRadius) {
                    caught.add(b);
                }
            }
        }
        return caught;
    }
}


