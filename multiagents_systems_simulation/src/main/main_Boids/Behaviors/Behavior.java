package main.main_Boids.Behaviors;
import main.main_Boids.Boids.*;
import main.main_Boids.Boidutils.*;


public interface Behavior {
    // Method to compute the behavior force for a given boid and grid
    public Vector2D behave(Boid boid, Grid grid);
    // Method to update the boid's position in the grid
    public void updateGrid(Boid b, Grid grid);
    // Method to get the type of grid used by the behavior which will determine how boids are stored in the grid
    public GridType getGridType();
}

