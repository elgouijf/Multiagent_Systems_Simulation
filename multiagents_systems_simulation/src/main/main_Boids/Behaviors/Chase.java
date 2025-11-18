package main.main_Boids.Behaviors;

import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.Grid;
import main.main_Boids.Boidutils.GridType;
import main.main_Boids.Boidutils.Vector_2D;
import java.util.ArrayList;

public class Chase implements Behavior {

    private double forceFactor;      // how strong the chase is
    private double detectionRadius;  // how far the eagle sees prey

    public Chase(double forceFactor, double detectionRadius) {
        this.forceFactor = forceFactor;
        this.detectionRadius = detectionRadius;
    }

    @Override
    public Vector_2D behave(Boid b, Grid grid) {
        ArrayList<Boid> neighbors = grid.getNeighbors(b);

        Boid closest = null;
        double minDist = Double.MAX_VALUE;

        // find closest bird (prey)
        for (Boid other : neighbors) {

            if (other.getSpecies().equals("Birds")) {   // ← si tu veux, on peut remplacer par instanceof
                double d = b.distance_to(other);

                if (d < minDist && d < detectionRadius) {
                    minDist = d;
                    closest = other;
                }
            }
        }

        if (closest == null) {
            return new Vector_2D(); // nothing to chase
        }

        // Move toward prey
        Vector_2D desired = closest.getPosition().copy();
        desired.subtract(b.getPosition());
        desired.updateMagnitude(b.getSpeedlimit());  // full speed toward prey

        Vector_2D steering = b.getSteeringForce(desired);
        steering.multiply(forceFactor);
        return steering;
    }

    @Override
    public void updateGrid(Boid b, Grid grid) {
        grid.updateBoidCell(b);
    }

    @Override
    public GridType getGridType() {
        return GridType.TOGETHER;
    }
}
