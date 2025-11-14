package main;

public interface Behavior {
    public Vector_2D behave(Boid boid, Grid grid);
    public void updateGrid(Boid b, Grid grid);
    public GridType getGridType();
}

