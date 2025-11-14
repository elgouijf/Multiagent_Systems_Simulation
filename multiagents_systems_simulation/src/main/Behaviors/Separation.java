package main.Behaviors;
import main.Boids.*;
import main.Vector_2D;
import main.Grid;
import main.GridType;

import java.util.ArrayList;

public class Separation implements Behavior {
    private double forceFactor;

    public Separation(double factor, int width, int height, double separation_distance){
        this.forceFactor = factor;
    }

    public Separation(int width, int height, double separation_distance){
        this.forceFactor = 1.0;
    }

    @Override
    public Vector_2D behave(Boid b, Grid grid){
        int n_close_boids = 0;
        Vector_2D average_flee = new Vector_2D(); // intiialize to an empty vector
        
        /* ArrayList<Boid> listBoids = boids.getlisteBoids(); */
        ArrayList<Boid> list_potential_neighbors = grid.getNeighbors(b);
        for (Boid otherboid : list_potential_neighbors){
            /* if ((otherboid != this) && 
            (distance_to(otherboid) < this.close_distance)){ */
            // update n_close_boids
            n_close_boids += 1;
            Vector_2D from_me_to_you = b.getPosition().copy();
            from_me_to_you.subtract(otherboid.getPosition());

            // the closer boid is to other the more it is urging to flee away
            double dist = b.distance_to(otherboid);

            if (dist > 0) {
                from_me_to_you.updateMagnitude(1.0 / dist);}
            // update average_flee
            average_flee.add(from_me_to_you);
            
        }
        
        if (n_close_boids >= 1){
            // divide by the number of close boids to get the average flee
            average_flee.divide(n_close_boids);
            // the boid wants to flee as fast as possible in the direction of the average_velocity vector
            average_flee.updateMagnitude(b.getSpeedlimit());
            Vector_2D separ_force = b.getSteeringForce(average_flee);
            separ_force.multiply(forceFactor);
            separ_force.limit(b.getforceLimit());

            // return Alignment force
            return separ_force;
        }
        return new Vector_2D(0,0);// no close boids detected
    }

    @Override
    public void updateGrid(Boid b, Grid grid){
        grid.updateBoidCell(b);
    }

    @Override
    public GridType getGridType(){
        return GridType.SEPARATION;
    }

 
}