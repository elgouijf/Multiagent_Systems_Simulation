package main.main_Boids.Behaviors;
import java.util.ArrayList;
import main.main_Boids.Boids.*;
import main.main_Boids.Boidutils.*;

public class Alignment extends Behavior {
   
    public Alignment(double forceFactor, int width, int height, double Alignment_distance){
        super(forceFactor);   
    }

    public Alignment(int width, int height, double Alignment_distance){
        // default factor
        super(1);
    }

    @Override
    public Vector2D behave(Boid b, Grid grid){
        int nSightBoids = 0; // number of boids that are inSight for b
        Vector2D average_velocity = new Vector2D();
        ArrayList<Boid> list_potential_neighbors = grid.getNeighbors(b);

        for (Boid otherboid : list_potential_neighbors){
            /* if ((otherboid != b) && 
            (b.inSight(otherboid))){
                // update average_velocity */
            if (b.inSight(otherboid)){
                average_velocity.add(otherboid.getVelocity());
                nSightBoids++;
            }   
            
        }

        if (nSightBoids >= 1){
            // divide by number of close boids to get the average velocity
            average_velocity.divide(nSightBoids);

            // the boid wants to flee as fast as possible in the direction of the average_flee vector
            average_velocity.updateMagnitude(b.getSpeedlimit());
            Vector2D align_force = b.getSteeringForce(average_velocity);
            align_force.multiply(forceFactor);
            align_force.limit(b.getforceLimit());

            /* return align_force; */
            return align_force;
        }
        return new Vector2D(0,0);// no close boids detected
    }
    @Override
    public GridType getGridType(){
        // use TOGETHER to use neighbor_distance
        return GridType.TOGETHER;
    }
}