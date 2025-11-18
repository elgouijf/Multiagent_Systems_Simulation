package main.main_Boids.Boids.Species;

import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.*;
import main.main_Boids.Behaviors.*;
import java.awt.Color;
import java.util.ArrayList;

public class Eagle extends Boid {
    private double killRadius;

    public Eagle(Vector_2D position, Vector_2D velocity, Vector_2D acceleration,
                 double speedLimit, double forceLimit, double wander_radius, double path_radius,
                 int boid_size, Color color, Color compassColor, double angleDistance,
                 int windowWidth, int windowHeight) {

        super(position, velocity, acceleration, speedLimit, forceLimit,
                wander_radius, path_radius, boid_size,
                color, compassColor, angleDistance, windowWidth, windowHeight);

        this.species = "Eagle";
        this.killRadius = 6.0;

        // Eagles wander much less
        this.wander_factor = 0.25;

        initializeBehaviors(windowWidth, windowHeight);
    }

    public void initializeBehaviors(int width, int height) {
        this.behaviors.clear();
        // Eagles avoid bumping but slightly
        this.behaviors.add(new Separation(0.4, width, height, this.close_distance * 1.5));

        // Very low alignment
        this.behaviors.add(new Alignment(width, height, this.neighbor_distance));

        // Very low cohesion
        this.behaviors.add(new Cohesion(0.3, width, height, this.neighbor_distance * 1.2));

        // Predation → strongest
        this.behaviors.add(new Chase(2.2, this.neighbor_distance * 3));
    }

    public double getPredationDistance() {
        return this.neighbor_distance * 3;
    }

    public ArrayList<Boid> hunt(ArrayList<Boid> potentialPrey) {
        ArrayList<Boid> caught = new ArrayList<>();
        for (Boid b : potentialPrey) {
            if (b instanceof Bird) {
                double d_square = this.distance_to_optimized(b);
                if (d_square < killRadius*killRadius) {
                    caught.add(b);
                }
            }
        }
        return caught;
    }
}


