package main.main_Boids.Behaviors;
import main.main_Boids.Boids.*;
import main.main_Boids.Boidutils.*;


public abstract class Behavior {
    protected  double forceFactor;

    public Behavior(double forceFactor){
        this.forceFactor = forceFactor;
    }
    
    public void updateGrid(Boid b, Grid grid) {
        grid.updateBoidCell(b);
    }
    // Method to compute the behavior force for a given boid and grid
    public abstract Vector2D behave(Boid boid, Grid grid);
    
    // Method to get the type of grid used by the behavior which will determine how boids are stored in the grid
    public abstract GridType getGridType();
}

