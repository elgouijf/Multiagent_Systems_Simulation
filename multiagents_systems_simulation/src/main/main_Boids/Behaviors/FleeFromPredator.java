package main.main_Boids.Behaviors;

import java.util.ArrayList;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.Grid;
import main.main_Boids.Boidutils.GridType;
import main.main_Boids.Boidutils.Vector2D;

public class FleeFromPredator extends  Behavior {

                                      // Fleeing force multiplier
    private double detectionRadius;  // How far the boid can detect predators

    public FleeFromPredator(double forceFactor, double detectionRadius) {
        super(forceFactor);
        this.detectionRadius = detectionRadius;
    }

    @Override
    public Vector2D behave(Boid b, Grid grid) {
        ArrayList<Boid> neighbors = grid.getNeighbors(b);

        Vector2D fleeForce = new Vector2D();

        for (Boid other : neighbors) {
            // Check if the other boid is a predator of this boid (Eagle for Birds, etc.)
            if (other.getPrey().equals(b.getSpecies())) {
                double d = b.distanceTo(other);
                if (d < detectionRadius) {
                    // Flee from the predator
                    Vector2D away = other.getPosition().copy();
                    away.subtract(b.getPosition());
                    away.multiply(-1); // opposite direction of the predator
                    away.updateMagnitude(b.getforceLimit()); // max force
                    fleeForce.add(away);
                }
            }
        }

        // Multiply by force factor and limit to max force
        fleeForce.multiply(forceFactor);
        fleeForce.limit(b.getforceLimit()); 
        return fleeForce;
    }
    @Override
    public GridType getGridType() {
        return GridType.TOGETHER; // use TOGETHER to use neighborDistance, only flee from close predators
    }
}
