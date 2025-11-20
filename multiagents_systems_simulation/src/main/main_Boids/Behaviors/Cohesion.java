package main.main_Boids.Behaviors;
import java.util.ArrayList;
import main.main_Boids.Boids.*;
import main.main_Boids.Boidutils.*;

public class Cohesion extends  Behavior {
    

    public Cohesion(double forceFactor, int width, int height, double Cohesion_distance){
        super(forceFactor);
    }

    public Cohesion(int width, int height, double Cohesion_distance){
        // default factor
        super(1);
    }

    @Override
    public Vector2D behave(Boid b, Grid grid){
        Vector2D average_pos = new Vector2D();
        double sumMass = 0;
        /* ArrayList<Boid> listBoids = boids.getlisteBoids(); */
        ArrayList<Boid> list_potential_neighbors = grid.getNeighbors(b);


        for (Boid otherboid : list_potential_neighbors){
            /* if ((otherboid != b) && 
            (b.inSight(otherboid))){ */
            if (b.inSight(otherboid)){
                double m = otherboid.getMass();
                sumMass += m;

                // Compute the weighted sum of positions
                Vector2D weighted_pos = otherboid.getPosition().copy();
                weighted_pos.multiply(m);

                // update average_pos
                average_pos.add(weighted_pos);
            }}
        if (sumMass > 0){
            // divide by the sum of masses to get the average position (aka the center of inertia)
            average_pos.divide(sumMass);
            Vector2D desired = average_pos.copy();
            Vector2D inertia_seek = b.seek(desired, forceFactor);
            return inertia_seek;
        }
        return new Vector2D(0,0);// no close boids detected
    }

    @Override
    public GridType getGridType(){
        return GridType.TOGETHER;
    }}
 
