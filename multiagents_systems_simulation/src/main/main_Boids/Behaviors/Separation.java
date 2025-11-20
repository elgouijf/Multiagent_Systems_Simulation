package main.main_Boids.Behaviors;
import java.util.ArrayList;
import main.main_Boids.Boids.*;
import main.main_Boids.Boidutils.*;

public class Separation extends Behavior {
    
    public Separation(double forceFactor, int width, int height, double separationDistance){
        super(forceFactor);
    }

    public Separation(int width, int height, double separationDistance){
        super(1);
    }

    @Override
    public Vector2D behave(Boid b, Grid grid){
        int nCloseBoids = 0; // number of close Boids
        Vector2D averageFlee = new Vector2D(); // intiialize to an empty vector
        
        /* ArrayList<Boid> listBoids = boids.getlisteBoids(); */
        ArrayList<Boid> listPotentialNeighbors = grid.getNeighbors(b);
        for (Boid otherboid : listPotentialNeighbors){
            /* if ((otherboid != this) && 
            (distanceTo(otherboid) < this.close_distance)){ */
            // update nCloseBoids
            nCloseBoids += 1;
            Vector2D fromMeToYou = b.getPosition().copy();
            fromMeToYou.subtract(otherboid.getPosition());

            // the closer boid is to other the more it is urging to flee away
            double dist = b.distanceTo(otherboid);

            if (dist > 0) {
                fromMeToYou.updateMagnitude(1.0 / dist);}
            // update averageFlee
            averageFlee.add(fromMeToYou);
            
        }
        
        if (nCloseBoids >= 1){
            // divide by the number of close boids to get the average flee
            averageFlee.divide(nCloseBoids);
            // the boid wants to flee as fast as possible in the direction of the average_velocity vector
            averageFlee.updateMagnitude(b.getSpeedlimit());
            Vector2D separForce = b.getSteeringForce(averageFlee);
            separForce.multiply(forceFactor);
            separForce.limit(b.getforceLimit());

            // return Alignment force
            return separForce;
        }
        return new Vector2D(0,0);// no close boids detected
    }

    @Override
    public GridType getGridType(){
        return GridType.SEPARATION;
    }

 
}