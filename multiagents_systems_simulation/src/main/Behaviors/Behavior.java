package main.Behaviors;
import main.Boids.*;
import main.Vector_2D;
import main.Grid;
import main.GridType;

public interface Behavior {
    public Vector_2D behave(Boid boid, Grid grid);
    public void updateGrid(Boid b, Grid grid);
    public GridType getGridType();
}

