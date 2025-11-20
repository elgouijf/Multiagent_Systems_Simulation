package main.main_Boids.Behaviors;

import java.util.ArrayList;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.Grid;
import main.main_Boids.Boidutils.GridType;
import main.main_Boids.Boidutils.Vector2D;

public class Chase extends Behavior {

   // how strong the chase is
    private double detectionRadius;  // how far the eagle sees prey

    public Chase(double forceFactor, double detectionRadius) {

        super(forceFactor);
        this.detectionRadius = detectionRadius;
    }

    @Override
    public Vector2D behave(Boid b, Grid grid) {
        ArrayList<Boid> neighbors = grid.getNeighbors(b);

        Boid closest = null;
        double minDist = Double.MAX_VALUE; // initialize to a large value

        // find closest bird (prey)
        for (Boid other : neighbors) {

            if (other.getSpecies().equals("Birds")) {   // is prey
                double d = b.distanceTo(other);

                if (d < minDist && d < detectionRadius) { // within detection radius
                    // the goal is to find the closest prey
                    minDist = d; // update minimum distance
                    closest = other;
                }
            }
        }

        if (closest == null) {
            return new Vector2D(); // nothing to chase
        }

        // Move toward prey
        Vector2D desired = closest.getPosition().copy();
        desired.subtract(b.getPosition());
        desired.updateMagnitude(b.getSpeedlimit());  // full speed toward prey

        Vector2D steering = b.getSteeringForce(desired);
        steering.multiply(forceFactor);
        steering.limit(b.getforceLimit()); // limit to max force
        return steering;
    }

    @Override
    public GridType getGridType() {
        return GridType.TOGETHER;
    }
}
