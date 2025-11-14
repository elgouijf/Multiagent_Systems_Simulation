package main;

import java.util.ArrayList;

public class Cohesion implements Behavior {
    private double forceFactor;

    public Cohesion(double factor, int width, int height, double Cohesion_distance){
        this.forceFactor = factor;
    }

    public Cohesion(int width, int height, double Cohesion_distance){
        this.forceFactor = 1.0;
    }

    @Override
    public Vector_2D behave(Boid b, Grid grid){
        Vector_2D average_pos = new Vector_2D();
        double sum_mass = 0;
        /* ArrayList<Boid> listBoids = boids.getlisteBoids(); */
        ArrayList<Boid> list_potential_neighbors = grid.getNeighbors(b);


        for (Boid otherboid : list_potential_neighbors){
            /* if ((otherboid != b) && 
            (b.inSight(otherboid))){ */
            if (b.inSight(otherboid)){
                double m = otherboid.getMass();
                sum_mass += m;

                // Compute the weighted sum of positions
                Vector_2D weighted_pos = otherboid.getPosition().copy();
                weighted_pos.multiply(m);

                // update average_pos
                average_pos.add(weighted_pos);
            }}
        if (sum_mass > 0){
            // divide by the sum of masses to get the average position (aka the center of inertia)
            average_pos.divide(sum_mass);
            Vector_2D desired = average_pos.copy();
            Vector_2D inertia_seek = b.seek(desired, forceFactor);
            return inertia_seek;
        }
        return new Vector_2D(0,0);// no close boids detected
    }
    @Override
    public void updateGrid(Boid b, Grid grid){
        grid.updateBoidCell(b);
    }
    @Override
    public GridType getGridType(){
        return GridType.TOGETHER;
    }}
 
