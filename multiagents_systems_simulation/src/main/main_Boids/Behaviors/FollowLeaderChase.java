package main.main_Boids.Behaviors;

import java.util.Random;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boids.Species.Wolf;
import main.main_Boids.Boidutils.Grid;
import main.main_Boids.Boidutils.GridType;
import main.main_Boids.Boidutils.Vector_2D;
import main.main_Boids.Boids.Species.Wolf;

import java.util.ArrayList;
import java.util.Random;



public class FollowLeaderChase implements Behavior{
   
    private double forceFactor;

    public FollowLeaderChase(double forceFactor){
        this.forceFactor = forceFactor;
    }    

    @Override
    public Vector_2D behave(Boid b,Grid grid){
        Wolf wolf = (Wolf) b; // nécessaire afin de compiler
        double distanceChase = wolf.getSlowRadius();
        Boid prey = wolf.getPrey();
        double distancetoPrey = wolf.distance_to(prey);
        if (distancetoPrey < distanceChase){
           Random rand = new Random();
           double angle = rand.nextDouble()*Math.PI*2;
           Vector_2D circlePrey = new Vector_2D(distance*Math.cos(angle),distance*Math.sin(angle));
           circlePrey.add(prey);
           return wolf.seek(circlePrey,forceFactor);
        }else{
            int indexLeader = wolf.getindexLeader();
            ArrayList<Wolf> pack = wolf.getPack();
            Vector_2D target = pack.get(indexLeader).getPosition();
            return wolf.seek(target,forceFactor);
        }
    }
    
    @Override
    public void updateGrid(Boid b, Grid grid) {
        grid.updateBoidCell(b);
    }

    @Override
    public GridType getGridType() {
        return GridType.TOGETHER;
    }

}