package main;

import java.util.ArrayList;

public class Alignment implements Behavior {
    private double forceFactor;
    private Grid grid;

    public Alignment(double factor, int width, int height, double Alignment_distance){
        this.forceFactor = factor;
        this.grid = new Grid(width, height, Alignment_distance, Grid.GridType.TOGETHER);;
    }

    public Alignment(int width, int height, double Alignment_distance){
        this.forceFactor = 1.0;
        this.grid = new Grid(width, height, Alignment_distance, Grid.GridType.TOGETHER);;
    }

    @Override
    public Vector_2D behave(Boid b){
        int n_sight_boids = 0;
        Vector_2D average_velocity = new Vector_2D();
        ArrayList<Boid> list_potential_neighbors = grid.getNeighbors(b);

        for (Boid otherboid : list_potential_neighbors){
            /* if ((otherboid != b) && 
            (b.inSight(otherboid))){
                // update average_velocity */
            if (b.inSight(otherboid)){
                average_velocity.add(otherboid.getVelocity());
                n_sight_boids++;
            }   
            
        }

        if (n_sight_boids >= 1){
            // divide by number of close boids to get the average velocity
            average_velocity.divide(n_sight_boids);

            // the boid wants to flee as fast as possible in the direction of the average_flee vector
            average_velocity.updateMagnitude(b.getSpeedlimit());
            Vector_2D align_force = b.getSteeringForce(average_velocity);
            align_force.multiply(forceFactor);
            align_force.limit(b.getforceLimit());

            /* return align_force; */
            return align_force;
        }
        return new Vector_2D(0,0);// no close boids detected
    }
 
}