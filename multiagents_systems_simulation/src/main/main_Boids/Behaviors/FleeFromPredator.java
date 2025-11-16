package main.main_Boids.Behaviors;

import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.Grid;
import main.main_Boids.Boidutils.GridType;
import main.main_Boids.Boidutils.Vector_2D;
import java.util.ArrayList;

public class FleeFromPredator implements Behavior {

    private double forceFactor;      // intensité de la fuite
    private double detectionRadius;  // distance à laquelle l'oiseau détecte un prédateur

    public FleeFromPredator(double forceFactor, double detectionRadius) {
        this.forceFactor = forceFactor;
        this.detectionRadius = detectionRadius;
    }

    @Override
    public Vector_2D behave(Boid bird, Grid grid) {
        ArrayList<Boid> neighbors = grid.getNeighbors(bird);

        Vector_2D fleeForce = new Vector_2D();

        for (Boid other : neighbors) {
            // Vérifie si c'est un prédateur (instance Eagle)
            if (other instanceof main.main_Boids.Boids.Species.Eagle) {
                double d = bird.distance_to(other);

                if (d < detectionRadius) {
                    // Force opposée à la position du prédateur
                    Vector_2D away = other.getPosition().copy();
                    away.subtract(bird.getPosition());
                    away.multiply(-1); // direction opposée
                    away.updateMagnitude(bird.getforceLimit()); // max force
                    fleeForce.add(away);
                }
            }
        }

        // Multiplier par le facteur de force
        fleeForce.multiply(forceFactor);
        return fleeForce;
    }

    @Override
    public void updateGrid(Boid b, Grid grid) {
        grid.updateBoidCell(b);
    }

    @Override
    public GridType getGridType() {
        return GridType.TOGETHER; // utilise neighbor_distance
    }
}
