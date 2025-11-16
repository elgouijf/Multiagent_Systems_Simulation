package main.main_Boids.Behaviors;
import main.main_Boids.Boids.*;
import main.main_Boids.Boidutils.*;


public interface Behavior {
    public Vector_2D behave(Boid boid, Grid grid);
    public void updateGrid(Boid b, Grid grid);
    public GridType getGridType();
}

